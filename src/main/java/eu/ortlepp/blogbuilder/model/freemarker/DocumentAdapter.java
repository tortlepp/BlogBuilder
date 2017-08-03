package eu.ortlepp.blogbuilder.model.freemarker;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.util.Tools;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.WrappingTemplateModel;

/**
 * An adapter for Document data objects in FreeMarker templates. Some fields of the Document data object are
 * exposed for the use in FreeMarker templates.
 *
 * @author Thorsten Ortlepp
 */
public class DocumentAdapter extends WrappingTemplateModel implements AdapterTemplateModel, TemplateHashModel {

    /** The Document data object. */
    protected final Document document;

    /** The wrapper for the adapter. */
    private final ObjectWrapper wrapper;


    /**
     * Constructor, initializes the adapter.
     *
     * @param document The Document data object
     * @param wrapper The wrapper for the adapter
     */
    public DocumentAdapter(final Document document, final ObjectWrapper wrapper) {
        super(wrapper);
        this.document = document;
        this.wrapper = wrapper;
    }


    /**
     * Returns the Document data object.
     *
     * @param hint A hint for the object
     * @return The Document data object
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdaptedObject(final Class hint) {
        return document;
    }


    /**
     * Get a TemplateModel and its value from the Document data object.
     *
     * @param key The key for the desired value
     * @return The TemplateModel and the value for the given key
     * @throws TemplateModelException Error while creating the suitable TemplateModel
     */
    @Override
    public TemplateModel get(final String key) throws TemplateModelException {
        switch (key) {
            case "title":
                return new StringModel(document.getTitle());
            case "content":
                return new StringModel(getPreparedContent());
            case "link":
                return new StringModel(document.getPath());
            case "previous":
                return new StringModel(document.getPrevious());
            case "next":
                return new StringModel(document.getNext());
            case "created":
                return new DateModel(document.getCreated());
            case "modified":
                return new DateModel(document.getModified());
            case "categories":
                return new CategoryListModel(document.getCategories(), wrapper);
            default:
                return null;
        }
    }


    /**
     * Prepare the content for the output: Get the HTML and create relative links with full paths.
     *
     * @return The prepared content
     */
    protected String getPreparedContent() {
        return Tools.makeLinksRelative(document.getContentAsHtml(), document.getToBaseDir());
    }


    /**
     * Check if the adapter is empty. Always returns false because the adapter is never empty.
     *
     * @return Always false (the adapter is never empty)
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

}
