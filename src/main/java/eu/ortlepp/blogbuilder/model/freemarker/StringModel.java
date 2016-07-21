package eu.ortlepp.blogbuilder.model.freemarker;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * A model to handle string values from a Document data object.
 *
 * @author Thorsten Ortlepp
 */
public class StringModel implements TemplateScalarModel {

    /** The string value. */
    private final String string;


    /**
     * Constructor, initialize the string value.
     *
     * @param string The string value
     */
    public StringModel(final String string) {
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
