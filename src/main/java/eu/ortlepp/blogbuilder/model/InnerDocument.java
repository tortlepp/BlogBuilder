package eu.ortlepp.blogbuilder.model;

/**
 * Data object for a document (a Markdown file). This data object has no own logic. If an InnerDocument is created
 * from a Document the will contain the same values. The only reason why InnerDocument exists is, that if a HTML page
 * that contains several blog posts (e.g. the index or category pages), these inner / embedded blog posts will contain
 * malformed relative links after the content of the page is processed with the usual DocumentAdapter. To process the
 * inner blog posts with another Adapter they need to be another type. With InnerDocument the inner blog posts can be
 * processed by the InnerDocumentAdapter.
 *
 * @author Thorsten Ortlepp
 */
public class InnerDocument extends Document {

    /**
     * Copy constructor, initialize the InnerDocument with the values from the Document.
     *
     * @param document The Document to clone
     */
    public InnerDocument(final Document document) {
        super(document.getFile(), document.getPath(), document.getToBaseDir());
        setTitle(document.getTitle());
        setCreated(document.getCreated());
        setModified(document.getModified());
        setBlog(document.isBlog());
        addContent(document.getContent());
        setPrevious(document.getPrevious());
        setNext(document.getNext());
        setCategories(document.getCategories());
    }

}
