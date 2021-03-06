package eu.ortlepp.blogbuilder.util.xml;

import eu.ortlepp.blogbuilder.util.config.Config;
import eu.ortlepp.blogbuilder.util.config.Directories;

import org.w3c.dom.Element;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A creator for sitemaps. Creates a sitemap which contains all blog posts, pages and index pages.
 *
 * @author Thorsten Ortlepp
 */
public final class SitemapCreator extends AbstractXmlCreator {

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
    public SitemapCreator(final String directory) {
        super();
        initRootElement();
        this.directory = Paths.get(directory, Directories.BLOG.toString()).toString();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        baseurl = Config.INSTANCE.getBaseUrl();
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
    public void createSitemap(final List<eu.ortlepp.blogbuilder.model.Document> blogposts,
            final List<eu.ortlepp.blogbuilder.model.Document> pages) {
        /* Add all blog posts and pages to the sitemap */
        addDocuments(blogposts);
        addDocuments(pages);

        /* Get a list of all files (will be null if directory is not a directory) */
        final String[] files = new File(directory).list();

        /* Add all index and category files to the sitemap */
        if (files != null) {
            for (final String file : files) {
                if (file.matches(Config.INSTANCE.getIndexFile() + "(-\\d+)*\\.html")
                        || file.matches(Config.INSTANCE.getCategoryFile() + "(.)+\\.html")) {
                    addUrl(baseurl + file, LocalDateTime.now());
                }
            }
        }

        /* Write XML to file */
        writeXmlFile(new File(directory, Config.INSTANCE.getSitemapFile()));
    }


    /**
     * Add all Document objects of a list to the XML document.
     *
     * @param documents The list of Document objects
     */
    private void addDocuments(final List<eu.ortlepp.blogbuilder.model.Document> documents) {
        for (final eu.ortlepp.blogbuilder.model.Document document : documents) {
            addUrl(baseurl + document.getPath(), document.getModified());
        }
    }


    /**
     * Add a URL (with its last modification date) to the XML document.
     *
     * @param loc The URL to add to the XML document
     * @param lastmod The modification date of the URL
     */
    private void addUrl(final String loc, final LocalDateTime lastmod) {
        final Element url = xmlDocument.createElement("url");
        url.appendChild(createElement("loc", loc));
        url.appendChild(createElement("lastmod", lastmod.format(dateFormatter)));
        xmlRoot.appendChild(url);
    }

}
