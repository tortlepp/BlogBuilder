package eu.ortlepp.blogbuilder;

import eu.ortlepp.blogbuilder.action.Build;
import eu.ortlepp.blogbuilder.action.Initialize;
import eu.ortlepp.blogbuilder.model.Parameter;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is the main class for BlogBuilder. It contains the main() method which starts the application and handles the
 * command line arguments.
 *
 * @author Thorsten Ortlepp
 */
public final class BlogBuilder {

    /** The current version of the application. */
    private static final String VERSION = "0.6";

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(BlogBuilder.class.getName());

    /** Message to print if no directory / project is specified at the start of the program. */
    private static final String MESSAGE_NO_DIR = "No directory / project specified, start-up aborted";


    /**
     * Main method to start the application. The first first parameter is checked for validity;
     * if it is valid the program continues the start, otherwise a usage info is printed.
     *
     * @param args Parameters for the application; all valid parameters are defined in Parameters
     *
     */
    public static void main(final String... args) {
        if (args.length >= 1 && Parameter.isValidParam(args[0])) {
            /* Check if a second parameter exists */
            final String optional = args.length >= 2 ? args[1] : "";
            new BlogBuilder().run(args[0], optional);
        } else {
            printUsageInfo();
        }
    }


    /**
     * Constructor, initializes the logging environment.
     */
    private BlogBuilder() {
        try {
            LogManager.getLogManager().readConfiguration(this.getClass().getClassLoader()
                    .getResourceAsStream("eu/ortlepp/blogbuilder/config/logging.properties"));
        } catch (SecurityException | IOException ex) {
            throw new RuntimeException("Initializing the logger failed", ex);
        }
    }


    /**
     * Run the application, start the action specified by the first command line argument.
     *
     * @param action The action to start (first command line argument); has to be validated before
     * @param optional An optional parameter for the action (second command line argument); could
     *     be an empty string but must not be null
     */
    private void run(final String action, final String optional) {

        if (Parameter.INITIALIZE.toString().equalsIgnoreCase(action)) {
            /* Run the initialization action */
            if (optional.isEmpty()) {
                LOGGER.severe(MESSAGE_NO_DIR);
            } else {
                new Initialize(optional).run();
            }

        } else if (Parameter.BUILD.toString().equalsIgnoreCase(action)) {
            /* Run the build action */
            if (optional.isEmpty()) {
                LOGGER.severe(MESSAGE_NO_DIR);
            } else {
                new Build(optional).run();
            }

        } else {
            /* Action is validated before, this error should never happen */
            throw new AssertionError("No action for valid parameter!");
        }
    }


    /**
     * Print usage instructions for the application.
     */
    private static void printUsageInfo() {
        System.out.printf("BlogBuilder %s - Usage:%n", VERSION);
        for (final Parameter param : Parameter.values()) {
            System.out.println(param.getParamInfo());
        }
    }

}
