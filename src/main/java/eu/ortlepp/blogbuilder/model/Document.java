package eu.ortlepp.blogbuilder.model;

import org.pegdown.PegDownProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data object for a document (a Markdown file). If multiple objects are sorted in a list the sorting is done by the
 * creation date (with most recent date at the beginning).
 *
 * @author Thorsten Ortlepp
 */
public class Document implements Comparable<Document> {

    /** The name and path of the file. */
    private final Path file;

    /** The relative path of the HTML file. */
    private final String path;

    /** The relative path from the document to the base dir. */
    private final String toBaseDir;

    /** The title of the document. */
    private String title;

    /** The creation date of the document. */
    private LocalDateTime created;

    /** The modification date of the document. */
    private LocalDateTime modified;

    /** Document type: Is the document a blog post (true) or a simple page (false). */
    private boolean blog;

    /** The content of the document. */
    private final StringBuilder content;

    /** A link to the previous (earlier) blog post. */
    private String previous;

    /** A link to the next (newer) blog post. */
    private String next;

    /** A shortlink to the document (its HTML page). */
    private String shortlink;

    /** A list that contains the categories of the document. */
    private final List<Category> categories;

    /** A Markdown processor to transform the content from Markdown to HTML. */
    private static final PegDownProcessor PROCESSOR;


    /**
     * Static initializer, initialize the Markdown processor.
     */
    static {
        PROCESSOR = new PegDownProcessor();
    }


    /**
     * Constructor, initialize the document with empty / default values. The document type is set to blog post.
     *
     * @param file Name and path of the file
     * @param path The relative path of the HTML file
     * @param toBaseDir The relative path from the document to the base dir
     */
    public Document(final Path file, final String path, final String toBaseDir) {
        this.file = file;
        this.path = path;
        this.toBaseDir = toBaseDir;
        this.title = "";
        this.created = LocalDateTime.MIN;
        this.modified = LocalDateTime.MIN;
        this.blog = true;
        this.content = new StringBuilder();
        this.previous = "";
        this.next = "";
        this.shortlink = "";
        this.categories = new ArrayList<Category>();
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
     * Getter for the relative path of the HTML file.
     *
     * @return Relative path of the HTML file
     */
    public String getPath() {
        return path;
    }


    /**
     * Getter for the relative path from the document to the base dir.
     *
     * @return Relative path from the document to the base dir
     */
    public String getToBaseDir() {
        return toBaseDir;
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
    public void setTitle(final String title) {
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
    public void setCreated(final LocalDateTime created) {
        this.created = created;
    }


    /**
     * Getter for the modification date of the document. If no modification date was set the creation date of the
     * document is returned.
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
    public void setModified(final LocalDateTime modified) {
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
     * Setter for the document type (blog post or simple page).
     *
     * @param blog Document type; true = blog post, false = simple page
     */
    protected void setBlog(final boolean blog) {
        this.blog = blog;
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
    public void addContent(final String part) {
        content.append(part);
    }


    /**
     * Getter for the link to the previous (earlier) blog post.
     *
     * @return Link to the previous blog post
     */
    public String getPrevious() {
        return previous;
    }


    /**
     * Setter for the link to the previous (earlier) blog post.
     *
     * @param previous Link to the previous blog post
     */
    public void setPrevious(final String previous) {
        this.previous = previous;
    }


    /**
     * Getter for the link to the next (newer) blog post.
     *
     * @return Link to the next blog post
     */
    public String getNext() {
        return next;
    }


    /**
     * Setter for the link to the next (newer) blog post.
     *
     * @param next Link to the next blog post
     */
    public void setNext(final String next) {
        this.next = next;
    }


    /**
     * Getter for the shortlink of the document.
     *
     * @return Shortlink of the document
     */
    public String getShortlink() {
        return shortlink;
    }


    /**
     * Setter for the shortlink of the document.
     *
     * @param shortlink Shortlink of the document
     */
    public void setShortlink(final String shortlink) {
        this.shortlink = shortlink;
    }


    /**
     * Getter for the list that contains the categories of the document.
     *
     * @return The list of categories
     */
    public List<Category> getCategories() {
        return categories;
    }


    /**
     * Setter for the list that contains the categories of the document.
     *
     * @param categories The list of categories to set
     */
    protected void setCategories(final List<Category> categories) {
        categories.forEach(this.categories::add);
    }


    /**
     * Adds a new category to the list of categories of the document. The new category is only added if the list does
     * not contain the category so far.
     *
     * @param category A new category of the document
     */
    public void addCategory(final String category) {
        final Category temp = new Category(category, toBaseDir);

        if (!categories.contains(temp)) {
            categories.add(temp);
        }
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


    /**
     * Compare the Document to another document (used to sort lists).
     *
     * @param other The document to which this document is compared
     * @return The result of the comparison
     */
    @Override
    public int compareTo(final Document other) {
        if (getCreated().isBefore(other.getCreated())) {
            return 1;
        } else if (getCreated().isAfter(other.getCreated())) {
            return -1;
        }
        return 0;
    }


    /**
     * Custom implementation of equals(). Two documents are equal if their original files are the same.
     *
     * @param object The other document to compare to this document
     * @return The result of the comparison; true = the documents (their original files) are equal,
     *     false = the documents (their original files) are not equal or the other object is not a document
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof Document) {
            final Document document = (Document) object;
            try {
                return Files.isSameFile(document.getFile(), getFile());
            } catch (IOException ex) {
                return false;
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
        return Objects.hash(file, path, toBaseDir, title, created, modified,
                blog, content, previous, next, shortlink, categories);
    }

}
