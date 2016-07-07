package eu.ortlepp.blogbuilder.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

import org.pegdown.PegDownProcessor;

/**
 * Data object for a document (a Markdown file).
 *
 * @author Thorsten Ortlepp
 */
public class Document {

    /** The name and path of the file. */
    private final Path file;

    /** The title of the document. */
    private String title;

    /** The creation date of the document. */
    private LocalDateTime created;

    /** The modification date of the document. */
    private LocalDateTime modified;

    /** Document type: Is the document a blog post (true) or a simple page (false). */
    private boolean blog;

    /** The content of the document. */
    private StringBuilder content;

    /** A Markdown processor to transform the content from Markdown to HTML. */
    private static final PegDownProcessor PROCESSOR;


    /**
     * Static initializer, initialize the Markdown processor.
     */
    static {
        PROCESSOR = new PegDownProcessor();
    }


    /**
     * Constructor, initialize the document with empty / default values. The document type is set to
     * blog post.
     *
     * @param file Name and path of the file
     */
    public Document(Path file) {
        this.file = file;
        this.title = "";
        this.created = LocalDateTime.MIN;
        this.modified = LocalDateTime.MIN;
        this.blog = true;
        this.content = new StringBuilder();
    }


    /**
     * Getter for the name and path of the file.
     *
     * @return Name and path of the file
     */
    public Path getFile() {
        return file;
    }


    /**
     * Getter for the title of the document.
     *
     * @return Title of the document
     */
    public String getTitle() {
        return title;
    }


    /**
     * Setter for the title of the document.
     *
     * @param title Title of the document
     */
    public void setTitle(String title) {
        this.title = title.trim();
    }


    /**
     * Getter for the creation date of the document.
     *
     * @return Creation date of the document
     */
    public LocalDateTime getCreated() {
        return created;
    }


    /**
     * Setter for the creation date of the document.
     *
     * @param created Creation date of the document
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }


    /**
     * Getter for the modification date of the document. If no modification date was set
     * the creation date of the document is returned.
     *
     * @return Modification date of the document
     */
    public LocalDateTime getModified() {
        if (modified.isEqual(LocalDateTime.MIN)) {
            return created;
        }
        return modified;
    }


    /**
     * Setter for the modification date of the document.
     *
     * @param modified Modification date of the document
     */
    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }


    /**
     * Getter for the document type (blog post or simple page).
     *
     * @return Document type; true = blog post, false = simple page
     */
    public boolean isBlog() {
        return blog;
    }


    /**
     * Setter for the document type - changes the document type to simple page.
     */
    public void setNoBlog() {
        this.blog = false;
    }


    /**
     * Getter for the content of the document.
     *
     * @return Content of the document
     */
    public String getContent() {
        return content.toString().trim();
    }


    /**
     * Getter for the content of the document. The content is returned as HTML.
     *
     * @return Content of the document as HTML
     */
    public String getContentAsHtml() {
        return PROCESSOR.markdownToHtml(getContent());
    }


    /**
     * Add a string to the content of the document. The string is appended at the end of the content.
     *
     * @param part The string to add
     */
    public void addContent(String part) {
        content.append(part);
    }


    /**
     * Check if the document is valid. A document is valid if it has a title, content and a creation date.
     *
     * @return Result of the check; true = document is valid, false = document is not valid
     */
    public boolean isValidDocument() {
        if (title.isEmpty() || getContent().isEmpty() || created.isEqual(LocalDateTime.MIN)) {
            return false;
        }
        return true;
    }

}
