package eu.ortlepp.blogbuilder.model.freemarker;

import eu.ortlepp.blogbuilder.model.Category;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

import java.util.List;

/**
 * A model to handle the category list from the Document data object.
 *
 * @author Thorsten Ortlepp
 */
public class CategoryListModel implements TemplateCollectionModel {

    /** The category list. */
    private final List<Category> categories;

    /** The wrapper for the adapter. */
    private final ObjectWrapper wrapper;


    /**
     * Constructor, initialize the category list.
     *
     * @param categories The category list
     * @param wrapper The wrapper for the adapter
     */
    public CategoryListModel(final List<Category> categories, final ObjectWrapper wrapper) {
        this.categories = categories;
        this.wrapper = wrapper;
    }


    /**
     * Returns the iterator of the list.
     *
     * @return The iterator of the list
     * @throws TemplateModelException Error while returning the iterator
     */
    @Override
    public TemplateModelIterator iterator() throws TemplateModelException {
        return new SimpleCollection(categories, wrapper).iterator();
    }

}
