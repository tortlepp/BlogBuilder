package eu.ortlepp.blogbuilder.model.freemarker;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.model.EmbeddedDocument;
import freemarker.template.ObjectWrapper;

/**
 * An adapter for EmbeddedDocument data objects in FreeMarker templates. The only difference to DocumentAdapter is the
 * handling of the content of the Document. EmbeddedDocumentAdapter leaves the content untouched and does not change
 * the links it contains.
 *
 * @author Thorsten Ortlepp
 */
public class EmbeddedDocumentAdapter extends DocumentAdapter {

    /**
     * Constructor, casts the given EmbeddedDocument to Document and calls super().
     *
     * @param document The (Embedded)Document data object
     * @param wrapper The wrapper for the adapter
     */
    public EmbeddedDocumentAdapter(final EmbeddedDocument document, final ObjectWrapper wrapper) {
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
