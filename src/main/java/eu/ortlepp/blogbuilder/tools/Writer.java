package eu.ortlepp.blogbuilder.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.model.freemarker.DocumentWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * A writer for HTML files. Writes the processed Markdown files to HTML files.
 *
 * @author Thorsten Ortlepp
 */
public class Writer {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Writer.class.getName());

    /** The directory for processed files (= the target directory). */
    private final Path dirBlog;

    /** The directory for raw files (= the source directory). */
    private final Path dirContent;

    /** The configuration of the Freemarker template engine. */
    private final Configuration fmConfig;


    /**
     * Constructor, initializes the Freemarker template engine.
     *
     * @param directory The directory of the project
     */
    public Writer(Path directory) {
        this.dirBlog = Paths.get(directory.toString(), "blog");
        this.dirContent = Paths.get(directory.toString(), "content");

        fmConfig = new Configuration(Configuration.VERSION_2_3_25);
        try {
            fmConfig.setDirectoryForTemplateLoading(new File(directory.toFile(), "templates"));
            fmConfig.setDefaultEncoding("UTF-8");
            fmConfig.setLocale(Locale.getDefault());
            fmConfig.setObjectWrapper(new DocumentWrapper(fmConfig.getIncompatibleImprovements()));
        } catch (IOException ex) {
            LOGGER.severe("Initializing Freemarker failed!");
            throw new RuntimeException(ex);
        }
    }


    /**
     * Write documents of the list to HTML files. The template for blog posts is used.
     *
     * @param blogposts The list of blog posts
     */
    public void writeBlogPosts(List<Document> blogposts) {
        int written = writeDocuments(blogposts, "post", "page_blogpost.ftl");
        LOGGER.info(String.format("%d blog posts written", written));
    }


    /**
     * Write documents of the list to HTML files. The template for simple pages is used.
     *
     * @param pages The list of simple pages
     */
    public void writePages(List<Document> pages) {
        int written = writeDocuments(pages, "page", "page_page.ftl");
        LOGGER.info(String.format("%d pages written", written));
    }


    /**
     * Write documents to HTML files. All documents of the list are processed and written to disk.
     *
     * @param documents The list of documents to write
     * @param key The key which makes the document accessible in the template
     * @param template The template file to use for the HTML files
     * @return Returns the number of HTML files that were written
     */
    public int writeDocuments(List<Document> documents, String key, String template) {
        int counter = 0;
        Map<String, Object> content = new HashMap<String, Object>();

        for (Document document : documents) {
            /* Set the document */
            if (content.containsKey(key)) {
                content.replace(key, document);
            } else {
                content.put(key, document);
            }

            /* Get the relative path of the file and change the extension to .html */
            String relFile = dirContent.relativize(document.getFile()).toString();
            relFile = relFile.substring(0, relFile.lastIndexOf('.')) + ".html";

            /* The absolute path of the file */
            Path file = Paths.get(dirBlog.toString(), relFile);

            try {
                /* Create directories */
                Files.createDirectories(file.getParent());

                /* Write file to disk using the Freemarker template */
                try (java.io.Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.toFile()), Charset.forName("UTF-8")))) {
                    Template fmTemplate = fmConfig.getTemplate(template);
                    fmTemplate.process(content, out);
                }

                counter++;
                LOGGER.info(String.format("Wrote %s", relFile));

            } catch (IOException | TemplateException ex) {
                LOGGER.severe(String.format("Writin %s failed: %s", relFile, ex.getMessage()));
            }
        }

        return counter;
    }

}
