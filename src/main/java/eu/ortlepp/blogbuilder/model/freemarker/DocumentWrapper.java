package eu.ortlepp.blogbuilder.model.freemarker;

import eu.ortlepp.blogbuilder.model.Document;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

/**
 * A wrapper for Document data objects in Freemarker templates.
 *
 * @author Thorsten Ortlepp
 */
public class DocumentWrapper extends DefaultObjectWrapper {

    /**
     * Constructor, only calls super().
     *
     * @param version Incompatible Improvements
     */
    public DocumentWrapper(final Version version) {
        super(version);
    }


    /**
     * Add handler for document data objects in Freemarker templates. For objects of type Document a custom
     * adapter is created; all other object types will be handled by the super class.
     *
     * @param object Object to be handled by Freemarker
     * @return The TemplateModel for the object
     * @throws TemplateModelException Error while creating the handler
     */
    @Override
    protected TemplateModel handleUnknownType(final Object object) throws TemplateModelException {
        if (object instanceof Document) {
            return new DocumentAdapter((Document) object, this);
        }
        return super.handleUnknownType(object);
    }

}
