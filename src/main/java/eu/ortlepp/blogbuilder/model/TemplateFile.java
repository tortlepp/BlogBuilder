package eu.ortlepp.blogbuilder.model;

/**
 * An Enum for FreeMarker template files. Files that are in this Enum are expected to
 * exist for the build process as BlogBuilder uses them to write the different types
 * of HTML pages.
 *
 * @author Thorsten Ortlepp
 */
public enum TemplateFile {

    /** Template file for blog posts. */
    BLOGPOST("page_blogpost.ftl"),

    /** Template file for content pages. */
    PAGE("page_page.ftl"),

    /** Template file for index pages. */
    INDEX("page_index.ftl"),

    /** Template file for category pages. */
    CATEGORY("page_category.ftl");


    /** The template file associated with the Enum value. */
    private final String file;


    /**
     * Constructor, initializes an Enum value with the given template file.
     *
     * @param file The template file for the Enum value
     */
    TemplateFile(final String file) {
        this.file = file;
    }


    /**
     * Custom implementation of the toString method, returns the template file that is
     * associated with the Enum value.
     *
     * @return The template file associated with the Enum value
     */
    @Override
    public String toString() {
        return file;
    }


    /**
     * Returns the template file that is associated with the Enum value; it is prefixed
     * by the sub-folder in which it is located in the samples folder.
     *
     * @return The template file prefixed with the sub-folder where it is located
     */
    public String getResourcePath() {
        return "templates/" + file;
    }

}
