package eu.ortlepp.blogbuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import eu.ortlepp.blogbuilder.action.Build;
import eu.ortlepp.blogbuilder.action.Initialize;

/**
 * This is the main class for BlogBuilder. It contains the main() method which starts the
 * application and handles the command line arguments.
 *
 * @author Thorsten Ortlepp
 */
public final class BlogBuilder {

    /** The current version of the application. */
    private static final String VERSION = "0.5";

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(BlogBuilder.class.getName());


    /**
     * Main method to start the application.
     *
     * @param args Arguments for the application; Two arguments are necessary to start:
     *  The first argument is the action to start (initialize or build), the second
     *  argument ist path to the project directory
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
     * Run the application, start the action specified by the first command line argument.
     *
     * @param action The action to do (first command line argument)
     * @param directory The directory / project (second command line argument)
     */
    private void run(String action, String directory) {
        Path path = Paths.get(directory);

        /* Initialization */
        if (action.equalsIgnoreCase("--init")) {
            LOGGER.info(String.format("Starting initialization for %s", path.getFileName()));

            if (Files.exists(path)) {
                LOGGER.severe(String.format("Directory %s already exists, initialization aborted", path.getFileName()));
            } else {
                Initialize.initialize(path);
            }
        }

        /* Build */
        else if (action.equalsIgnoreCase("--build")) {
            LOGGER.info(String.format("Starting build process for %s", path.getFileName()));

            if (Files.exists(path) && Files.isDirectory(path)) {
                Build.build(path);
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
        System.out.println(" -init <DIRECTORY>   - Initialize a new project in <DIRECTORY>");
        System.out.println(" -build <DIRECTORY>  - Build the project in <DIRECTORY>");
    }

}
