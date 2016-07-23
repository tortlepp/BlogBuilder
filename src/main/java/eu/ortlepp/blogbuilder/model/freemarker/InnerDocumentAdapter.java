package eu.ortlepp.blogbuilder.model.freemarker;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.model.InnerDocument;
import freemarker.template.ObjectWrapper;

/**
 * An adapter for InnerDocument data objects in Freemarker templates. The only difference to DocumentAdapter is the
 * handling of the content of the Document. InnerDocumentAdapter leaves the content untouched and does not change
 * the links it contains.
 *
 * @author Thorsten Ortlepp
 */
public class InnerDocumentAdapter extends DocumentAdapter {

    /**
     * Constructor, casts the given InnerDocument to Document and calls super().
     *
     * @param document The (Inner)Document data object
     * @param wrapper The wrapper for the adapter
     */
    public InnerDocumentAdapter(final InnerDocument document, final ObjectWrapper wrapper) {
        super((Document) document, wrapper);
    }


    /**
     * Prepare the content for the output: Get the HTML (and don't do anything else).
     *
     * @return The prepared content
     */
    @Override
    protected String getPreparedContent() {
        return document.getContentAsHtml();
    }

}
