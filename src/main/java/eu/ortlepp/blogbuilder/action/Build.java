package eu.ortlepp.blogbuilder.action;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.util.Cleaner;
import eu.ortlepp.blogbuilder.util.ResourceCopy;
import eu.ortlepp.blogbuilder.util.Scanner;
import eu.ortlepp.blogbuilder.util.Writer;
import eu.ortlepp.blogbuilder.util.config.Config;
import eu.ortlepp.blogbuilder.util.config.Directories;
import eu.ortlepp.blogbuilder.util.xml.FeedCreator;
import eu.ortlepp.blogbuilder.util.xml.SitemapCreator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Action: Build an existing project.
 *
 * @author Thorsten Ortlepp
 */
public final class Build implements Action {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Build.class.getName());

    /** The directory of the project to build. */
    private final Path directory;

    /** The list which contains all blog posts. */
    private List<Document> blogposts;

    /** The list which contains all simple pages. */
    private final List<Document> pages;


    /**
     * Constructor, prepare the build process.
     *
     * @param directory Directory of the project to build
     */
    public Build(final String directory) {
        this.directory = Paths.get(directory);
        blogposts = new ArrayList<Document>();
        pages = new ArrayList<Document>();
    }


    /**
     * Run the build process step by step.
     */
    @Override
    public void run() {
        if (Files.exists(directory) && Files.isDirectory(directory)) {
            LOGGER.info(String.format("Starting build process for %s", directory.getFileName()));

            Config.INSTANCE.loadConfig(directory.toFile());
            Cleaner.clean(Paths.get(directory.toString(), Directories.BLOG.toString()));
            scanDirectory();
            writeFiles();
            ResourceCopy.copy(directory);
            new FeedCreator(blogposts, directory.toString()).createFeed();
            new SitemapCreator(directory.toString()).createSitemap(blogposts, pages);

        } else {
            LOGGER.severe(String.format("Directory %s does not exist, build aborted", directory.getFileName()));
        }
    }


    /**
     * Scan the content directory and find all Markdown files.
     */
    private void scanDirectory() {
        /* Find all Markdown files */
        blogposts = new Scanner().scanDirectory(Paths.get(directory.toString(), Directories.CONTENT.toString()));

        /* Copy pages to pages list and remove them from blog post list */
        final Iterator<Document> iterator = blogposts.iterator();
        while (iterator.hasNext()) {
            final Document document = iterator.next();
            if (!document.isBlog()) {
                pages.add(document);
                iterator.remove();
            }
        }

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
    private String getRelaviveLink(final Document current, final Document other) {
        String link = current.getFile().relativize(other.getFile()).toString();
        link = link.replaceAll("\\\\", "/");
        link = link.substring(0, link.lastIndexOf('.')) + ".html";
        link = link.replaceFirst("../", "");
        return link;
    }


    /**
     * Write all blog posts, pages and special pages to HTML files.
     */
    private void writeFiles() {
        final Writer writer = new Writer(Paths.get(directory.toString(), Directories.BLOG.toString()),
                Paths.get(directory.toString(), Directories.TEMPLATES.toString()));
        writer.writeBlogPosts(blogposts);
        writer.writePages(pages);
        writer.writeIndex(blogposts);
        writer.writeCategoryPages(blogposts);
    }

}
