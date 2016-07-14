package eu.ortlepp.blogbuilder.tools;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.w3c.dom.Element;

/**
 * A creator for Atom Feeds. Creates an Atom Feed which contains the most recent blog posts.
 *
 * @author Thorsten Ortlepp
 */
public class FeedCreator extends AbstractXmlCreator {

    /** A list with all blog posts in chronological order. */
    private final List<eu.ortlepp.blogbuilder.model.Document> blogposts;

    /** The file to create. */
    private final File feed;

    /** Get access to configuration read from the properties file. */
    private final Config config;


    /**
     * Constructor, initializes the XML document and its root element. The creation of the feed is prepared.
     *
     * @param blogposts A list with all blog posts
     * @param directory The project directory
     */
    public FeedCreator(List<eu.ortlepp.blogbuilder.model.Document> blogposts, String directory) {
        super();
        initRootElement();
        this.blogposts = blogposts;
        this.feed = Paths.get(directory, Config.DIR_BLOG, Config.getInstance().getFeedFile()).toFile();
        this.config = Config.getInstance();
    }


    /**
     * Initialize the root element of the feed.
     */
    @Override
    protected void initRootElement() {
        xmlRoot = xmlDocument.createElement("feed");
        xmlRoot.setAttributeNode(createAttribute("xmlns", "http://www.w3.org/2005/Atom"));
        xmlDocument.appendChild(xmlRoot);
    }


    /**
     * Create the Atom feed and write it to a physical file.
     */
    public void createFeed() {
        createFeedInfo();
        addBlogPosts();
        writeFeed(feed);
    }


    /**
     * Create all elements with information about the feed and append the elements to the feed.
     */
    private void createFeedInfo() {
        /* <title> */
        xmlRoot.appendChild(createElement("title", config.getTitle()));

        /* <id> */
        xmlRoot.appendChild(createElement("id", createId(config.getBaseUrl(), LocalDateTime.parse("2016-01-01T12:00:00"), config.getFeedFile())));

        /* <link> */
        Element link = xmlDocument.createElement("link");
        link.setAttributeNode(createAttribute("href", config.getBaseUrl()));
        link.setAttributeNode(createAttribute("rel", "self"));
        xmlRoot.appendChild(link);

        /* <updated> */
        LocalDateTime updated = LocalDateTime.MIN;
        for (int i = 0; i < config.getFeedPosts(); i++) {
            if (i < blogposts.size()) {
                if (blogposts.get(i).getModified().isAfter(updated)) {
                    updated = blogposts.get(i).getModified();
                }
            } else {
                break;
            }
        }
        xmlRoot.appendChild(createElement("updated", formatDateTime(updated)));

        /* <author> (contains <name>) */
        Element author = xmlDocument.createElement("author");
        author.appendChild(createElement("name", config.getAuthor()));
        xmlRoot.appendChild(author);
    }


    /**
     * Add blog posts to the feed. The number of posts to add is set in the configuration file.
     */
    private void addBlogPosts() {
        for (int i = 0; i < config.getFeedPosts(); i++) {

            if (i < blogposts.size()) {
                eu.ortlepp.blogbuilder.model.Document document = blogposts.get(i);

                /* The root element of the blog post / entry */
                Element entry = xmlDocument.createElement("entry");

                /* <title> */
                entry.appendChild(createElement("title", document.getTitle()));

                /* <id> */
                entry.appendChild(createElement("id", createId(config.getBaseUrl(), document.getCreated(), document.getPath())));

                /* <updated> */
                entry.appendChild(createElement("updated", formatDateTime(document.getModified())));

                /* <link> */
                Element link = xmlDocument.createElement("link");
                link.setAttributeNode(createAttribute("href", config.getBaseUrl() + "/" + document.getPath()));
                entry.appendChild(link);

                /* <content> */
                Element content = createElement("content", Tools.makeLinksAbsolute(document.getContentAsHtml()));
                content.setAttributeNode(createAttribute("type", "html"));
                entry.appendChild(content);

                xmlRoot.appendChild(entry);

            } else {
                break;
            }
        }
    }


    /**
     * Create good IDs for the feed. An example of an ID: tag:example.com,2016-07-13:/example/document.html
     *
     * @param baseurl The base URL of the blog
     * @param created A (creation) date for the ID
     * @param file A filename with path
     * @return The created ID
     */
    private String createId(String baseurl, LocalDateTime created, String file) {
        String url = baseurl.replaceAll("http[s]*://", "");
        String date = created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.format("tag:%s,%s:/%s", url, date, file);
    }


    /**
     * Create a formatted date / time for the use in an Atom Feed. Therefore the local date / time is converted UTC.
     *
     * @param datetime The date / time to format
     * @return The formatted date / time
     */
    private String formatDateTime(LocalDateTime datetime) {
        ZonedDateTime zoned = datetime.atZone(ZoneId.systemDefault());
        return zoned.format(DateTimeFormatter.ISO_INSTANT);
    }

}
