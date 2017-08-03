package eu.ortlepp.blogbuilder.model;

/**
 * Data object for a document (a Markdown file). This data object has no own logic. If an EmbeddedDocument is created
 * from a Document, it will contain the same values. The only reason why EmbeddedDocument exists is, that if a HTML
 * page that contains several blog posts (e.g. the index or category pages), these embedded blog posts will contain
 * malformed relative links after the content of the page is processed with the usual DocumentAdapter. To process the
 * inner blog posts with another Adapter they need to be another type. With EmbeddedDocument the embedded blog posts
 * can be processed by the EmbeddedDocumentAdapter.
 *
 * @author Thorsten Ortlepp
 */
public class EmbeddedDocument extends Document {

    /**
     * Copy constructor, initialize the EmbeddedDocument with the values from the Document.
     *
     * @param document The Document to clone
     */
    public EmbeddedDocument(final Document document) {
        super(document.getFile(), document.getPath(), document.getToBaseDir());
        setTitle(document.getTitle());
        setCreated(document.getCreated());
        setModified(document.getModified());
        setType(document.getType());
        addContent(document.getContent());
        setPrevious(document.getPrevious());
        setNext(document.getNext());
        setCategories(document.getCategories());
    }

}
