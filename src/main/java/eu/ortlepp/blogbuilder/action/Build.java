package eu.ortlepp.blogbuilder.action;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.tools.Scanner;

/**
 * Action: Build a project.
 *
 * @author Thorsten Ortlepp
 */
public final class Build {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Build.class.getName());

    /** The directory of the project to build. */
    private final Path directory;

    /** The list which contains all blog posts. */
    private List<Document> blogposts;

    /** The list which contains all simple pages. */
    private List<Document> pages;


    /**
     * Run the build process for the project.
     *
     * @param directory Directory of the project to build
     */
    public static void build(Path directory) {
        new Build(directory).process();
    }


    /**
     * Constructor, prepare the build process.
     *
     * @param directory
     */
    private Build(Path directory) {
        this.directory = directory;
        blogposts = new ArrayList<Document>();
        pages = new ArrayList<Document>();
    }


    /**
     * Run the build process step by step.
     */
    private void process() {
        scanDirectory();
    }


    /**
     * Scan the content directory and find all Markdown files.
     */
    private void scanDirectory() {
        /* Find all Markdown files */
        blogposts = new Scanner().scanDirectory(Paths.get(directory.toString(), "content"));

        /* Copy pages to pages list */
        for (Document doc : blogposts) {
            if (!doc.isBlog()) {
                pages.add(doc);
            }
        }

        /* Remove simple pages from blog post list */
        blogposts.removeIf(new Predicate<Document>() {
            @Override
            public boolean test(Document document) {
                return !document.isBlog();
            }
        });

        LOGGER.info(String.format("Scan completed, found %d blog posts and %d pages", blogposts.size(), pages.size()));
    }

}
