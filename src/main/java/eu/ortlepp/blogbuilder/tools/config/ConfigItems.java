package eu.ortlepp.blogbuilder.tools.config;

/**
 * An Enum for configuration items. Each item has a key (to locate the item in the properties
 * file) and a default value (wich is used when a project is initialized and when an item
 * was not fount in a property file).
 *
 * @author Thorsten Ortlepp
 */
public enum ConfigItems {

    /** The configuration item for the title of the blog. */
    TITLE("blog.title", "My Blog"),

    /** The configuration item for the author of the blog. */
    AUTHOR("blog.author", "John Doe"),

    /** The configuration item for the name of the index file. */
    INDEX_FILE("index.filename", "index"),

    /** The configuration item for the number of blog posts on each index page. */
    INDEX_POSTS("index.posts", "3"),

    /** The configuration item for the filename of the feed. */
    FEED_FILE("feed.filename", "feed.xml"),

    /** The configuration item for the number of blog posts in the feed. */
    FEED_POSTS("feed.posts", "3"),

    /** The configuration item for the filename of the category pages. */
    CATEGORY_FILE("category.filename", "category_"),

    /** The configuration item for the base URL of the blog. */
    BASEURL("blog.baseurl", "http://blog.example.com"),

    /** The configuration item for the filename of the sitemap. */
    SITEMAP_FILE("sitemap.filename", "sitemap.xml"),

    /** The configuration item for the files to ignore while cleaning the blog directory. */
    CLEAN_IGONRE("clean.ignore", ".gitkeep"),

    /** The configuration item for the locale (used for number and date formats.) */
    LOCALE("blog.locale", "en-US");


    /** The key (in the properties file) of a configuration item. */
    private final String key;


    /** The default value of a configuration item. */
    private final String defaultValue;


    /**
     * Constructor to initialize a configuration item.
     *
     * @param key The key of a configuration item
     * @param default The default value of a configuration item
     */
    ConfigItems(final String key, final String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }


    /**
     * Getter for the key of the configuration item.
     *
     * @return The key of a configuration item
     */
    public String getKey() {
        return key;
    }


    /**
     * Getter for the default value of the configuration item.
     *
     * @return The default value of a configuration item
     */
    public String getDefaultValue() {
        return defaultValue;
    }


    /**
     * Custom implementation of the toString method, returns the key and its default value
     * separated by an equals sign. Example: key=default_value.
     *
     * @return The key and default value of the item
     */
    @Override
    public String toString() {
        return String.format("%s=%s", key, defaultValue);
    }

}
