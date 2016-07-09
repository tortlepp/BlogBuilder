package eu.ortlepp.blogbuilder.action;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.tools.Cleaner;
import eu.ortlepp.blogbuilder.tools.Scanner;
import eu.ortlepp.blogbuilder.tools.Writer;

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
        Cleaner.clean(Paths.get(directory.toString(), "blog"));
        scanDirectory();
        writeFiles();
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

        /* Sort the blog posts by creation date (most recent first) */
        Collections.sort(blogposts);

        /* Create links to the previous and next blog post */
        for (int i = 0; i < blogposts.size(); i++) {
            /* Set next blog post, but the first one has no next blog post */
            if (i != 0) {
                blogposts.get(i).setNext(getRelaviveLink(blogposts.get(i), blogposts.get(i - 1)));
            }

            /* Set previous blog post, but the last one has no previous blog post */
            if (i != blogposts.size() - 1) {
                blogposts.get(i).setPrevious(getRelaviveLink(blogposts.get(i), blogposts.get(i + 1)));
            }
        }

        LOGGER.info(String.format("Scan completed, found %d blog posts and %d pages", blogposts.size(), pages.size()));
    }


    /**
     * Create a link to the previous or next HTML file.
     *
     * @param doc1 The current document
     * @param doc2 The previous or next document
     * @return The link to the previous or next HTML file
     */
    private String getRelaviveLink(Document current, Document other) {
        String link = current.getFile().relativize(other.getFile()).toString();
        link = link.replaceAll("\\\\", "/");
        link = link.substring(0, link.lastIndexOf('.')) + ".html";
        link = link.replaceFirst("../", "");
        return link;
    }


    /**
     * Write all blog posts and pages to HTML files.
     */
    private void writeFiles() {
        Writer writer = new Writer(directory);
        writer.writeBlogPosts(blogposts);
        writer.writePages(pages);
        writer.writeIndex(blogposts);
    }

}
