package eu.ortlepp.blogbuilder.gui;

import java.awt.Font;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JTextArea;

/**
 * An extended JTextArea for displaying log records. The logger is directly connected to the
 * text area. The implementation is based on a Stack Overflow post by chipukb.
 *
 * @author Thorsten Ortlepp
 * @see <a href="https://stackoverflow.com/a/17841401">Stack Overflow post by chipukb</a>
 * @since 0.8
 */
class JLoggingTextArea extends JTextArea {

    /** Serial Version UID. Generated by Eclipse IDE. */
    private static final long serialVersionUID = 6793892442290104442L;


    /**
     * Constructor, initialize the TextArea, set the appearance and behavior and connect the logger
     * to the GUI component.
     */
    public JLoggingTextArea() {
        super();

        /* Setting appearance and behavior */
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize()));
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);

        /* Connecting the logger to the TextArea */
        Logger.getLogger("").addHandler(new TextAreaHandler(this));
    }


    /**
     * Append a string to the TextArea. When the string is appended, the TextArea scroll to its bottom.
     */
    @Override
    public void append(final String string) {
        super.append(string);
        setCaretPosition(getDocument().getLength());
    }






    /**
     * A custom handler for logging records. The logged records are directed into a GUI TextArea.
     * The implementation is based on a Stack Overflow post by chipukb.
     *
     * @author Thorsten Ortlepp
     * @see <a href="https://stackoverflow.com/a/17841401">Stack Overflow post by chipukb</a>
     * @since 0.8
     */
    private static class TextAreaHandler extends Handler {

        /** The TextArea where the logged records are directed to. */
        private final JLoggingTextArea textArea;

        /** Formatter for log records. The output format is already set when BlogBuilder starts. */
        private final SimpleFormatter formatter;


        /**
         * Constructor, initializes the handler.
         *
         * @param textArea The TextArea where the logged records are directed to
         */
        public TextAreaHandler(final JLoggingTextArea textArea) {
            this.textArea = textArea;
            formatter = new SimpleFormatter();
        }


        /**
         * Publish a log record. The record is added to the TextArea.
         */
        @Override
        public void publish(final LogRecord record) {
            if (isLoggable(record)) {
                synchronized (textArea) {
                    textArea.append(formatter.format(record));
                }
            }
        }


        /**
         * This method does nothing - no implementation is needed for this handler.
         */
        @Override
        public void flush() {
            /* Nothing to do here... */
        }


        /**
         * This method does nothing - no implementation is needed for this handler.
         */
        @Override
        public void close() throws SecurityException {
            /* Nothing to do here... */
        }
    }

}