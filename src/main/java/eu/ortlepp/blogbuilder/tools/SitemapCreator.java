package eu.ortlepp.blogbuilder.tools;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Element;

/**
 * A creator for sitemaps. Creates a sitemap which contains all blog posts, pages and index pages.
 *
 * @author Thorsten Ortlepp
 */
public class SitemapCreator extends AbstractXmlCreator {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(SitemapCreator.class.getName());

    /** Get access to configuration read from the properties file. */
    private final Config config;

    /** The directory with the built blog. */
    private final String directory;

    /** The base URL of the blog. */
    private String baseurl;

    /** Formatter to format dates. */
    private final DateTimeFormatter dateFormatter;


    /**
     * Constructor, initializes the XML document and its root element. The creation of the sitemap is prepared.
     *
     * @param directory The project directory
     */
    public SitemapCreator(String directory) {
        super();
        initRootElement();
        this.config = Config.getInstance();
        this.directory = Paths.get(directory, Config.DIR_BLOG).toString();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");;

        baseurl = config.getBaseUrl();
        if (!baseurl.endsWith("/")) {
            baseurl += "/";
        }
    }


    /**
     * Initialize the root element of the sitemap.
     */
    @Override
    protected void initRootElement() {
        xmlRoot = xmlDocument.createElement("urlset");
        xmlRoot.setAttributeNode(createAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9"));
        xmlDocument.appendChild(xmlRoot);
    }


    /**
     * Create the sitemap and write it to a physical file.
     *
     * @param blogposts A list off all blog posts
     * @param pages A list off all pages
     */
    public void createSitemap(List<eu.ortlepp.blogbuilder.model.Document> blogposts, List<eu.ortlepp.blogbuilder.model.Document> pages) {
        /* Add all blog posts and pages to the sitemap */
        addDocuments(blogposts);
        addDocuments(pages);

        /* Get a list of all files (will be null if directory is not a directory) */
        /* But usually this should never happen */
        String[] files = new File(directory).list();

        /* Add all index files to the sitemap */
        if (files == null) {
            LOGGER.warning(String.format("%s is not a directory", directory));
        } else {
            for (String file : files) {
                if (file.matches(config.getIndexFile() + "(-\\d+)*" + "\\.html")) {
                    addUrl(baseurl + file, LocalDateTime.now());
                }
            }
        }

        /* Write XML to file */
        writeFeed(new File(directory, config.getSitemapFile()));
    }


    /**
     * Add all Document objects of a list to the XML document.
     *
     * @param documents The list of Document objects
     */
    private void addDocuments(List<eu.ortlepp.blogbuilder.model.Document> documents) {
        for (eu.ortlepp.blogbuilder.model.Document document : documents) {
            addUrl(baseurl + document.getPath(), document.getModified());
        }
    }


    /**
     * Add a URL (with its last modification date) to the XML document.
     *
     * @param loc The URL to add to the XML document
     * @param lastmod The modification date of the URL
     */
    private void addUrl(String loc, LocalDateTime lastmod) {
        Element url = xmlDocument.createElement("url");
        url.appendChild(createElement("loc", loc));
        url.appendChild(createElement("lastmod", lastmod.format(dateFormatter)));
        xmlRoot.appendChild(url);
    }

}
