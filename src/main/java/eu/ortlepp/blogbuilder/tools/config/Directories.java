package eu.ortlepp.blogbuilder.tools.config;

/**
 * An Enum that conains the main directories of BlogBuilder.
 *
 * @author Thorsten Ortlepp
 */
public enum Directories {

    /** Directory for the built blog. */
    BLOG("blog"),

    /** Directory where the input files (in Markdown) are located. */
    CONTENT("content"),

    /** Directory which contains additional resources (CSS, images, ...) for the blog. */
    RESOURCES("resources"),

    /** Directory where the (Freemarker) templates are located. */
    TEMPLATES("templates");


    /** The name of the directory. */
    private final String name;


    /**
     * Constructor to initialize the name of the directory.
     *
     * @param name The name of the directory
     */
    Directories(final String name) {
        this.name = name;
    }


    /**
     * Custom implementation of the toString method, returns the name of the directory.
     *
     * @return The name of the directory
     */
    @Override
    public String toString() {
        return name;
    }

}
