package eu.ortlepp.blogbuilder.model.freemarker;

import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * A model to handle date and time values from a Document data object.
 *
 * @author Thorsten Ortlepp
 */
public class DateModel implements TemplateDateModel {

    /** The date and time value. */
    private final Date date;


    /**
     * Constructor, initialize the date and time value by converting the LocalDateTime to Date. The conversion is
     * necessary because Freemarker does not support LocalDateTime (from Java 8) so far.
     *
     * @param datetime The date and time value
     */
    public DateModel(final LocalDateTime datetime) {
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
        return new Date(date.getTime());
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
