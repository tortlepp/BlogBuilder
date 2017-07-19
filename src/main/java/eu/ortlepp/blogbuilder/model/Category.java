package eu.ortlepp.blogbuilder.model;

import eu.ortlepp.blogbuilder.util.config.Config;

import java.util.Locale;
import java.util.Objects;

/**
 * Data object for a category. Each category has a name. The data object also knows the path from the blog base
 * directory to the category page and the relative path from its parent object (a document / blog post) to the
 * category page.
 *
 * @author Thorsten Ortlepp
 */
public class Category {

    /** The name of the category. */
    private final String name;

    /** The path / filename of the category page. */
    private final String path;

    /** The relative path from the document to the category page. */
    private final String relativePath;

    /** The locale of the content / the name of the category. Used for string transformations. */
    private final Locale locale;


    /**
     * Constructor, initialize the category with the given name and path. The name of the category might be "fixed":
     * All white spaces (" ") are removed from the name.
     *
     * @param name The name of the category
     * @param toBaseDir The relative path from the parent object (a document / blog post) to the blog base directory
     */
    public Category(final String name, final String toBaseDir) {
        final String tmpName = name.replaceAll(" ", "");

        this.locale = Config.INSTANCE.getLocale();
        this.name = tmpName;
        this.path = String.format("%s%s.html", Config.INSTANCE.getCategoryFile(),
                tmpName.toLowerCase(Config.INSTANCE.getLocale()));
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
     * Getter for the name of the category. The name is formatted: The first letter of the name is capitalized while
     * all other letters are transformed to lower case.
     *
     * @return The formatted name of the category
     */
    public String getNameFormatted() {
        final String tmpName = name.toLowerCase(Config.INSTANCE.getLocale());
        return tmpName.substring(0, 1).toUpperCase(locale) + tmpName.substring(1);
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
     *     false = the categories (their names) are not equal or the other object is not a category
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Category) {
            final Category category = (Category) object;
            if (category.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Custom implementation of hashCode(), uses Objects.hash().
     *
     * @return The calculated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, path, relativePath, locale);
    }

}
