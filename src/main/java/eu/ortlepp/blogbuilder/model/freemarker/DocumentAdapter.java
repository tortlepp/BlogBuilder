package eu.ortlepp.blogbuilder.model.freemarker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import eu.ortlepp.blogbuilder.model.Category;
import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.tools.Tools;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateScalarModel;
import freemarker.template.WrappingTemplateModel;

/**
 * An adapter for Document data objects in Freemarker templates. Some fields of the Document data object are
 * exposed for the use in Freemarker templates.
 *
 * @author Thorsten Ortlepp
 */
public class DocumentAdapter extends WrappingTemplateModel implements AdapterTemplateModel, TemplateHashModel {

    /** The Document data object. */
    private final Document document;

    /** The wrapper for the adapter. */
    private final ObjectWrapper wrapper;

    /** Flag for fixing relative links in the content of a document; true = fix links (default behavior), false = leave links unchanged. */
    private static boolean fixContentLinks = true;


    /**
     * Constructor, initializes the adapter.
     *
     * @param document The Document data object
     * @param wrapper The wrapper for the adapter
     */
    public DocumentAdapter(Document document, ObjectWrapper wrapper) {
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
    @Override
    public Object getAdaptedObject(Class hint) {
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
    public TemplateModel get(String key) throws TemplateModelException {
        switch (key) {
            case "title":
                return new StringModel(document.getTitle());
            case "content":
                if (fixContentLinks) {
                    return new StringModel(Tools.makeLinksRelative(document.getContentAsHtml(), document.getToBaseDir()));
                } else {
                    return new StringModel(document.getContentAsHtml());
                }
            case "link":
                return new StringModel(document.getPath());
            case "previous":
                return new StringModel(document.getPrevious());
            case "next":
                return new StringModel(document.getNext());
            case "shortlink":
                return new StringModel(document.getShortlink());
            case "created":
                return new DateModel(document.getCreated());
            case "modified":
                return new DateModel(document.getModified());
            case "categories":
                return new CategoryListModel(document.getCategories());
            default:
                return null;
        }
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


    /**
     * Setter for the flag for fixing relative links in the content of a document.
     *
     * @param fix The value for the flag; true = fix links, false = leave links unchanged
     */
    public static void setFixContenLinks(boolean fix) {
        fixContentLinks = fix;
    }



    //////////  Inner classes  \\\\\\\\\\



    /**
     * A model to handle string values from the Document data object.
     *
     * @author Thorsten Ortlepp
     */
    private static class StringModel implements TemplateScalarModel {

        /** The string value. */
        private final String string;

        /**
         * Constructor, initialize the string value.
         *
         * @param string The string value
         */
        public StringModel(String string) {
            this.string = string;
        }

        /**
         * Returns the string value.
         *
         * @return The string value
         * @throws TemplateModelException Error while returning the string value
         */
        @Override
        public String getAsString() throws TemplateModelException {
            return string;
        }
    }


    /**
     * A model to handle date and time values from the Document data object.
     *
     * @author Thorsten Ortlepp
     */
    private static class DateModel implements TemplateDateModel {

        /** The date and time value. */
        private final Date date;

        /**
         * Constructor, initialize the date and time value by converting the LocalDateTime to Date.
         * The conversion is necessary because Freemarker does not support LocalDateTime so far.
         *
         * @param datetime The date and time value
         */
        public DateModel(LocalDateTime datetime) {
            this.date = Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
        }

        /**
         * Returns the date and time value.
         *
         * @return The date and time value
         * @throws TemplateModelException Error while returning the date and time value
         */
        @Override
        public Date getAsDate() throws TemplateModelException {
            return date;
        }

        /**
         * Returns the type of date. The type is always "date and time".
         *
         * @return Returns always "date and time"
         */
        @Override
        public int getDateType() {
            return TemplateDateModel.DATETIME;
        }
    }


    /**
     * A model to handle the category list from the Document data object.
     *
     * @author Thorsten Ortlepp
     */
    private class CategoryListModel implements TemplateCollectionModel {

        /** The category list. */
        private List<Category> categories;

        /**
         * Constructor, initialize the category list.
         *
         * @param categories The category list
         */
        public CategoryListModel(List<Category> categories) {
            this.categories = categories;
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

}
