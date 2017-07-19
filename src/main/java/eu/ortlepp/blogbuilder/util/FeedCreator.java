package eu.ortlepp.blogbuilder.util;

import eu.ortlepp.blogbuilder.util.config.Config;
import eu.ortlepp.blogbuilder.util.config.Directories;

import org.w3c.dom.Element;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A creator for Atom Feeds. Creates an Atom Feed which contains the most recent blog posts.
 *
 * @author Thorsten Ortlepp
 */
public final class FeedCreator extends AbstractXmlCreator {

    /** A list with all blog posts in chronological order. */
    private final List<eu.ortlepp.blogbuilder.model.Document> blogposts;

    /** The file to create. */
    private final File feed;


    /**
     * Constructor, initializes the XML document and its root element. The creation of the feed is prepared.
     *
     * @param blogposts A list with all blog posts
     * @param directory The project directory
     */
    public FeedCreator(final List<eu.ortlepp.blogbuilder.model.Document> blogposts, final String directory) {
        super();
        initRootElement();
        this.blogposts = blogposts;
        this.feed =
                Paths.get(directory, Directories.BLOG.toString(), Config.INSTANCE.getFeedFile()).toFile();
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
        xmlRoot.appendChild(createElement("title", Config.INSTANCE.getTitle()));

        /* <id> */
        xmlRoot.appendChild(createElement("id", createId(Config.INSTANCE.getBaseUrl(),
                LocalDateTime.parse("2016-01-01T12:00:00"), Config.INSTANCE.getFeedFile())));

        /* <link> */
        final Element link = xmlDocument.createElement("link");
        link.setAttributeNode(createAttribute("href", Config.INSTANCE.getBaseUrl()));
        link.setAttributeNode(createAttribute("rel", "self"));
        xmlRoot.appendChild(link);

        /* <updated> */
        LocalDateTime updated = LocalDateTime.MIN;
        for (int i = 0; i < Config.INSTANCE.getFeedPosts(); i++) {
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
        final Element author = xmlDocument.createElement("author");
        author.appendChild(createElement("name", Config.INSTANCE.getAuthor()));
        xmlRoot.appendChild(author);
    }


    /**
     * Add blog posts to the feed. The number of posts to add is set in the configuration file.
     */
    private void addBlogPosts() {
        for (int i = 0; i < Config.INSTANCE.getFeedPosts(); i++) {

            if (i < blogposts.size()) {
                final eu.ortlepp.blogbuilder.model.Document document = blogposts.get(i);

                /* The root element of the blog post / entry */
                final Element entry = xmlDocument.createElement("entry");

                /* <title> */
                entry.appendChild(createElement("title", document.getTitle()));

                /* <id> */
                entry.appendChild(createElement("id", createId(Config.INSTANCE.getBaseUrl(), document.getCreated(),
                        document.getPath())));

                /* <updated> */
                entry.appendChild(createElement("updated", formatDateTime(document.getModified())));

                /* <link> */
                final Element link = xmlDocument.createElement("link");
                link.setAttributeNode(createAttribute("href", Config.INSTANCE.getBaseUrl() + "/" + document.getPath()));
                entry.appendChild(link);

                /* <content> */
                final Element content = createElement("content", Tools.makeLinksAbsolute(document.getContentAsHtml()));
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
    private String createId(final String baseurl, final LocalDateTime created, final String file) {
        final String url = baseurl.replaceAll("http[s]*://", "");
        final String date = created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.format("tag:%s,%s:/%s", url, date, file);
    }


    /**
     * Create a formatted date / time for the use in an Atom Feed. Therefore the local date / time is converted UTC.
     *
     * @param datetime The date / time to format
     * @return The formatted date / time
     */
    private String formatDateTime(final LocalDateTime datetime) {
        final ZonedDateTime zoned = datetime.atZone(ZoneId.systemDefault());
        return zoned.format(DateTimeFormatter.ISO_INSTANT);
    }

}
