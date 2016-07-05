package eu.ortlepp.blogbuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is the main class for BlogBuilder. It contains the main() method which starts the
 * application and handles the console parameters.
 *
 * @author Thorsten Ortlepp
 */
public final class BlogBuilder {

    /** The current version of the application. */
    private static final String VERSION = "0.1";

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(BlogBuilder.class.getName());


    /**
     * Main method to start the application.
     *
     * @param args Parameters for the application; Two parameters are necessary to start:
     *  The first parameter is the action to start (initialize or build), the second
     *  parameter ist path to the project directory
     *
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            new BlogBuilder().run(args[0], args[1]);
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
            throw new RuntimeException(ex);
        }
    }


    /**
     * Run the application, start the action specified by the first parameter.
     *
     * @param action The action to do (first console parameter)
     * @param directory The directory / project (second console parameter)
     */
    private void run(String action, String directory) {
        Path path = Paths.get(directory);

        /* Initialization */
        if (action.equalsIgnoreCase("-i")) {
            LOGGER.info(String.format("Starting initialization for %s", path.getFileName()));

            if (Files.exists(path)) {
                LOGGER.severe(String.format("Directory %s already exists, initialization aborted", path.getFileName()));
            } else {
                //TODO INIT ...
            }
        }

        /* Build */
        else if (action.equalsIgnoreCase("-b")) {
            LOGGER.info(String.format("Starting build process for %s", path.getFileName()));

            if (Files.exists(path) && Files.isDirectory(path)) {
                //TODO BUILD ...
            } else {
                LOGGER.severe(String.format("Directory %s does not exist, build aborted", path.getFileName()));
            }
        }

        /* Unknown / undefined */
        else {
            printUsageInfo();
        }
    }


    /**
     * Print usage instructions for the application.
     */
    private static void printUsageInfo() {
        System.out.printf("BlogBuilder %s%s%s", VERSION, System.lineSeparator(), System.lineSeparator());
        System.out.println("Usage:");
        System.out.println(" -i <DIRECTORY>  - Initialize a new project in <DIRECTORY>");
        System.out.println(" -b <DIRECTORY>  - Build the project in <DIRECTORY>");
    }

}
