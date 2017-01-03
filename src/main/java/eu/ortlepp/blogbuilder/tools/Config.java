package eu.ortlepp.blogbuilder.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configuration for BlogBuilder and a project.
 *
 * @author Thorsten Ortlepp
 */
public final class Config {

    /** The configuration object itself (for singleton implementation). */
    private static Config config;

    /** Directory for the built blog. */
    public static final String DIR_BLOG = "blog";

    /** Directory where the input files (in Markdown) are located. */
    public static final String DIR_CONTENT = "content";

    /** Directory which contains additional resources (CSS, JavaScipt, images, ...) for the blog. */
    public static final String DIR_RESOURCES = "resources";

    /** Directory where the (Freemarker) templates are located. */
    public static final String DIR_TEMPLATES = "templates";

    /** The default value for the title of the blog. Used if no value is set in the configuration file. */
    private static final String DEFAULT_TITLE = "My Blog";

    /** The default value for the author of the blog. Used if no value is set in the configuration file. */
    private static final String DEFAULT_AUTHOR = "John Doe";

    /** The default value for the name of the index file. Used if no value is set in the configuration file. */
    private static final String DEFAULT_INDEX_FILE = "index";

    /**
     * The default value for the number of blog posts on each index page. Used if no value is set in the
     * configuration file.
     */
    private static final String DEFAULT_INDEX_POSTS = "3";

    /** The default value for the filename of the feed. Used if no value is set in the configuration file. */
    private static final String DEFAULT_FEED_FILE = "feed.xml";

    /**
     * The default value for the number of blog posts in the feed. Used if no value is set in the configuration file.
     */
    private static final String DEFAULT_FEED_POSTS = "3";

    /** The default value for the filename of the category pages. Used if no value is set in the configuration file. */
    private static final String DEFAULT_CATEGORY_FILE = "category_";

    /** The default value for the base URL of the blog. Used if no value is set in the configuration file. */
    private static final String DEFAULT_BASEURL = "http://blog.example.com";

    /** The default value for the filename of the sitemap. Used if no value is set in the configuration file. */
    private static final String DEFAULT_SITEMAP_FILE = "sitemap.xml";

    /** The title of the blog. */
    private String title;

    /** The author of the blog. */
    private String author;

    /** The name of the index file(s). */
    private String indexFile;

    /** The number of blog posts on each index page. */
    private int indexPosts;

    /** The filename of the feed. */
    private String feedFile;

    /** The filename of the category pages. */
    private String categoryFile;

    /** The number of blog posts in the feed. */
    private int feedPosts;

    /** The base URL of the blog. */
    private String baseurl;

    /** The locale to use (for number and date formats). */
    private Locale locale;

    /** The filename of the sitemap. */
    private String sitemapFile;

    /** Files in the "Blog" folder that are ignored while cleaning. */
    private String[] blogIgnore;


    /**
     * Constructor, initialize all configuration values with their defaults.
     */
    private Config() {
        title = DEFAULT_TITLE;
        author = DEFAULT_AUTHOR;
        indexFile = DEFAULT_INDEX_FILE;
        indexPosts = Integer.parseInt(DEFAULT_INDEX_POSTS);
        feedFile = DEFAULT_FEED_FILE;
        feedPosts = Integer.parseInt(DEFAULT_FEED_POSTS);
        categoryFile = DEFAULT_CATEGORY_FILE;
        baseurl = DEFAULT_BASEURL;
        locale = Locale.getDefault();
        sitemapFile = DEFAULT_SITEMAP_FILE;
        blogIgnore = new String[]{};
    }


    /**
     * Read the configuration from the properties file. The properties file is located in the project directory and
     * named blog.properties.
     *
     * @param directory The project directory which contains the properties file.
     */
    public void loadConfig(final File directory) {
        final String confFile = "blog.properties";

        try (FileInputStream fis = new FileInputStream(new File(directory, confFile))) {
            final Properties properties = new Properties();
            properties.load(fis);

            /* Read properties, use defaults if not found in properties file */
            title = properties.getProperty("blog.title", DEFAULT_TITLE);
            author = properties.getProperty("blog.author", DEFAULT_AUTHOR);
            indexFile = properties.getProperty("index.filename", DEFAULT_INDEX_FILE);
            feedFile = properties.getProperty("feed.filename", DEFAULT_FEED_FILE);
            categoryFile = properties.getProperty("category.filename", DEFAULT_CATEGORY_FILE);
            baseurl = properties.getProperty("blog.baseurl", DEFAULT_BASEURL);
            sitemapFile = properties.getProperty("sitemap.filename", DEFAULT_SITEMAP_FILE);
            blogIgnore = properties.getProperty("blog.ignore", "").split(";");

            try {
                indexPosts = Integer.parseInt(properties.getProperty("index.posts", DEFAULT_INDEX_POSTS));
            } catch (NumberFormatException ex) {
                Logger.getLogger(Config.class.getName()).warning(String.format("Parsing index.posts failed: %s",
                        ex.getMessage()));
            }

            try {
                feedPosts = Integer.parseInt(properties.getProperty("feed.posts", DEFAULT_FEED_POSTS));
            } catch (NumberFormatException ex) {
                Logger.getLogger(Config.class.getName()).warning(String.format("Parsing feed.posts failed: %s",
                        ex.getMessage()));
            }

            Locale localeTemp = Locale.forLanguageTag(properties.getProperty("blog.locale", ""));
            if (localeTemp.getLanguage().isEmpty() || localeTemp.getCountry().isEmpty()) {
                localeTemp = Locale.getDefault();
            }
            locale = localeTemp;

            Logger.getLogger(Config.class.getName()).info(String.format("Read configuration from %s", confFile));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).severe(String.format("Reading %s failed: %s",
                    confFile, ex.getMessage()));
            throw new RuntimeException(ex);
        }
    }


    /**
     * Get the one and only instance of the configuration object.
     *
     * @return The configuration object
     */
    public static synchronized Config getInstance() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }


    /**
     * Getter for the title of the blog.
     *
     * @return The title of the blog
     */
    public String getTitle() {
        return title;
    }


    /**
     * Getter for the author of the blog.
     *
     * @return The author of the blog
     */
    public String getAuthor() {
        return author;
    }


    /**
     * Getter for the name of the index file(s).
     *
     * @return The name of the index file(s)
     */
    public String getIndexFile() {
        return indexFile;
    }


    /**
     * Getter for the number of blog posts on each index page.
     *
     * @return The number of blog posts on each index page
     */
    public int getIndexPosts() {
        return indexPosts;
    }


    /**
     * Getter for the filename of the feed.
     *
     * @return The filename of the feed
     */
    public String getFeedFile() {
        return feedFile;
    }


    /**
     * Getter for the number of blog posts in the feed.
     *
     * @return The number of blog posts in the feed
     */
    public int getFeedPosts() {
        return feedPosts;
    }


    /**
     * Getter for the filename of the category pages.
     *
     * @return The filename of the category pages
     */
    public String getCategoryFile() {
        return categoryFile;
    }


    /**
     * Getter for the base URL of the blog.
     *
     * @return The base URL of the blog
     */
    public String getBaseUrl() {
        return baseurl;
    }


    /**
     * Getter for the locale to use (for number and date formats).
     *
     * @return The locale to use
     */
    public Locale getLocale() {
        return locale;
    }


    /**
     * Getter for the filename of the sitemap.
     *
     * @return The filename of the sitemap
     */
    public String getSitemapFile() {
        return sitemapFile;
    }


    /**
     * Getter for the Files in the "Blog" folder that are ignored while cleaning.
     *
     * @return Files in the "Blog" folder that are ignored while cleaning
     */
    public String[] getBlogIgnore() {
        return blogIgnore.clone();
    }

}
