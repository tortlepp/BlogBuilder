package eu.ortlepp.blogbuilder.tools.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configuration for BlogBuilder and a project. Implemented as singleton on Enum basis.
 *
 * @author Thorsten Ortlepp
 */
public enum Config {

    /** The (only) instance of Config. */
    INSTANCE;

    /** The name of the properties file. */
    private static final String CONFIG_FILE = "blog.properties";

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
    private String[] cleanIgnore;


    /**
     * Constructor, initialize all configuration values with their defaults.
     */
    Config() {
        title = ConfigItems.TITLE.getDefaultValue();
        author = ConfigItems.AUTHOR.getDefaultValue();
        indexFile = ConfigItems.INDEX_FILE.getDefaultValue();
        indexPosts = Integer.parseInt(ConfigItems.INDEX_POSTS.getDefaultValue());
        feedFile = ConfigItems.FEED_FILE.getDefaultValue();
        feedPosts = Integer.parseInt(ConfigItems.FEED_POSTS.getDefaultValue());
        categoryFile = ConfigItems.CATEGORY_FILE.getDefaultValue();
        baseurl = ConfigItems.BASEURL.getDefaultValue();
        locale = Locale.forLanguageTag(ConfigItems.LOCALE.getDefaultValue());
        sitemapFile = ConfigItems.SITEMAP_FILE.getDefaultValue();
        cleanIgnore = ConfigItems.CLEAN_IGONRE.getDefaultValue().split(";");
    }


    /**
     * Read the configuration from the properties file. The properties file is located in the project directory and
     * named blog.properties. If a configuration item is missing in the properties file, the default value for that
     * item is used.
     *
     * @param directory The project directory which contains the properties file.
     */
    public void loadConfig(final File directory) {
        try (FileInputStream fis = new FileInputStream(new File(directory, CONFIG_FILE))) {
            final Properties properties = new Properties();
            properties.load(fis);

            /* Read properties, use defaults if not found in properties file */
            title = getPropertyValue(properties, ConfigItems.TITLE);
            author = getPropertyValue(properties, ConfigItems.AUTHOR);
            indexFile = getPropertyValue(properties, ConfigItems.INDEX_FILE);
            feedFile = getPropertyValue(properties, ConfigItems.FEED_FILE);
            categoryFile = getPropertyValue(properties, ConfigItems.CATEGORY_FILE);
            baseurl = getPropertyValue(properties, ConfigItems.BASEURL);
            sitemapFile = getPropertyValue(properties, ConfigItems.SITEMAP_FILE);
            cleanIgnore = getPropertyValue(properties, ConfigItems.CLEAN_IGONRE).split(";");
            indexPosts = getPropertyIntValue(properties, ConfigItems.INDEX_POSTS);
            feedPosts = getPropertyIntValue(properties, ConfigItems.FEED_POSTS);

            Locale localeTemp = Locale.forLanguageTag(getPropertyValue(properties, ConfigItems.LOCALE));
            if (localeTemp.getLanguage().isEmpty() || localeTemp.getCountry().isEmpty()) {
                localeTemp = Locale.forLanguageTag(ConfigItems.LOCALE.getDefaultValue());
            }
            locale = localeTemp;

            Logger.getLogger(Config.class.getName())
                .info(String.format("Read configuration from %s", CONFIG_FILE));
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).severe(String.format("Reading %s failed: %s",
                    CONFIG_FILE, ex.getMessage()));
            throw new RuntimeException(ex);
        }
    }


    /**
     * Get a value from the properties object. If the value/item is not present,
     * the default value is returned instead.
     *
     * @param properties The properties read from file
     * @param configItem The property item whose value is requested
     * @return The value for the item; the default value if the item is not present
     */
    private String getPropertyValue(final Properties properties, final ConfigItems configItem) {
        return properties.getProperty(configItem.getKey(), configItem.getDefaultValue());
    }


    /**
     * Get an integer value from the properties object. If the value/item is not present,
     * the default value is returned instead. If the value from the properties object is
     * no valid integer, the default value is returned as well.
     *
     * @param properties The properties read from file
     * @param configItem The property item whose value is requested
     * @return The integer value for the item; the default value if the item is not present
     */
    private int getPropertyIntValue(final Properties properties, final ConfigItems configItem) {
        try {
            return Integer.parseInt(getPropertyValue(properties, configItem));
        } catch (NumberFormatException ex) {
            Logger.getLogger(Config.class.getName())
                .warning(String.format("Parsing %s failed (%s), using default value %s ",
                    configItem.getKey(), ex.getMessage(), configItem.getDefaultValue()));
            return Integer.parseInt(configItem.getDefaultValue());
        }
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
    public String[] getCleanIgnore() {
        return cleanIgnore.clone();
    }

}
