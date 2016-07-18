package eu.ortlepp.blogbuilder.model;

import eu.ortlepp.blogbuilder.tools.Config;

/**
 * Data object for a category. Each category has a name. The data object also knows the path
 * from the blog base directory to the category page and the relative path from its parent object
 * (a document / blog post) to the category page.
 *
 * @author Thorsten Ortlepp
 */
public class Category {

    /** The name of the category. */
    private String name;

    /** The path / filename of the category page. */
    private String path;

    /** The relative path from the document to the category page. */
    private String relativePath;


    /**
     * Constructor, initialize the category with the given name and path. The name of the category might be
     * "fixed": All white spaces (" ") are removed from the name.
     *
     * @param name The name of the category
     * @param toBaseDir The relative path from the parent object (a document / blog post) to the blog base directory
     */
    public Category(String name, String toBaseDir) {
        Config config = Config.getInstance();
        String tmpName = name.replaceAll(" ", "");

        this.name = tmpName;
        this.path = "category_" + tmpName.toLowerCase(config.getLocale()) + ".html";
        this.relativePath = toBaseDir + this.path;
    }


    /**
     * Getter for the name of the category.
     *
     * @return The name of the category
     */
    public String getName() {
        return name;
    }


    /**
     * Getter for the path / filename of the category page.
     *
     * @return The path / filename of the category page
     */
    public String getPath() {
        return path;
    }


    /**
     * Getter for the relative path from the document to the category page.
     *
     * @return The relative path from the document to the category page
     */
    public String getRelativePath() {
        return relativePath;
    }


    /**
     * Custom implementation of equals(). Two categories are equal if their names are equal.
     *
     * @param object The other category to compare to this category
     * @return The result of the comparison; true = the categories (their names) are equal,
     *  false = the categories (their names) are not equal or the other object is not a category
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Category) {
            Category category = (Category) object;
            if (category.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
