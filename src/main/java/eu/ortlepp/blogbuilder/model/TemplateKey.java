package eu.ortlepp.blogbuilder.model;

/**
 * An Enum for variables used in the Freemarker templates. Please note that this Enum is a
 * collection of some variables, but not all variables are available in each context. Also
 * note, that e.g. Document exposes its variables to the Freemarker templates - these
 * variables are not part of this Enum.
 *
 * @author Thorsten Ortlepp
 */
public enum TemplateKey {

    /** The template key for the path to the base directory. */
    BASEDIR("basedir"),

    /** The template key for next index page with newer blog posts. */
    INDEX_NEWER("index_newer"),

    /** The template key for previous index page with older blog posts. */
    INDEX_OLDER("index_older"),

    /** The template key for the name of the category. */
    CATEGORY("category");


    /** The template key associated with the Enum value. */
    private final String key;


    /**
     * Constructor, initializes an Enum value with the given template key.
     *
     * @param key The template key for the Enum value
     */
    TemplateKey(final String key) {
        this.key = key;
    }


    /**
     * Custom implementation of the toString method, returns the template key that is
     * associated with the Enum value.
     *
     * @return The template key associated with the Enum value
     */
    @Override
    public String toString() {
        return key;
    }






    /**
     * An Enum for special template keys which are used as prefixes for other template keys.
     * In a template, prefix and "real" template key are separated by a dot. Example:
     * blog.title - "blog" is the prefix, title the "real" template key.
     *
     * @author Thorsten Ortlepp
     */
    public enum Prefix {

        /** The prefix for static blog content (from the properties file). */
        BLOG("blog"),

        /** The prefix for the content of a single page. */
        PAGE("page"),

        /** The prefix for the content of a single (blog) post. */
        POST("post"),

        /** The prefix for the a list of inner documents ("sub" blog posts). */
        POSTS("posts");


        /** The template key associated with the Enum value. */
        private final String key;


        /**
         * Constructor, initializes an Enum value with the given template key.
         *
         * @param key The template key for the Enum value
         */
        Prefix(final String key) {
            this.key = key;
        }


        /**
         * Custom implementation of the toString method, returns the template key that is
         * associated with the Enum value.
         *
         * @return The template key associated with the Enum value
         */
        @Override
        public String toString() {
            return key;
        }
    }






    /**
     * An Enum for template keys for static blog content. The static content is set in the properties
     * file and directly available in the template by these keys.
     *
     * @author Thorsten Ortlepp
     */
    public enum Config {

        /** The template key for the title of the blog. */
        TITLE("title"),

        /** The template key for the author of the blog. */
        AUTHOR("author"),

        /** The template key for the language of the blog. */
        LANGUAGE("language");


        /** The template key associated with the Enum value. */
        private final String key;


        /**
         * Constructor, initializes an Enum value with the given template key.
         *
         * @param key The template key for the Enum value
         */
        Config(final String key) {
            this.key = key;
        }


        /**
         * Custom implementation of the toString method, returns the template key that is
         * associated with the Enum value.
         *
         * @return The template key associated with the Enum value
         */
        @Override
        public String toString() {
            return key;
        }
    }

}
