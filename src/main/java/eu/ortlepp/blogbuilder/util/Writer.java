package eu.ortlepp.blogbuilder.util;

import eu.ortlepp.blogbuilder.model.Category;
import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.model.InnerDocument;
import eu.ortlepp.blogbuilder.model.TemplateKey;
import eu.ortlepp.blogbuilder.model.freemarker.DocumentWrapper;
import eu.ortlepp.blogbuilder.util.config.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 * A writer for HTML files. Writes the processed Markdown files to HTML files.
 *
 * @author Thorsten Ortlepp
 */
public final class Writer {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Writer.class.getName());

    /** The target directory (where the HTML files are created). */
    private final Path target;

    /** The configuration of the Freemarker template engine. */
    private final Configuration fmConfig;

    /** Static data / information from the configuration file. */
    private final Map<String, String> blogInfo;


    /**
     * Constructor, initializes the Freemarker template engine and loads the static data.
     *
     * @param target The target directory (where the HTML files are created)
     * @param templates The directory which contains the templates
     */
    public Writer(final Path target, final Path templates) {
        this.target = target;

        /* Initialize Freemarker */
        fmConfig = new Configuration(Configuration.VERSION_2_3_25);
        try {
            fmConfig.setDirectoryForTemplateLoading(templates.toFile());
            fmConfig.setDefaultEncoding("UTF-8");
            fmConfig.setLocale(Config.INSTANCE.getLocale());
            fmConfig.setObjectWrapper(new DocumentWrapper(fmConfig.getIncompatibleImprovements()));
        } catch (IOException ex) {
            LOGGER.severe("Initializing Freemarker failed!");
            throw new RuntimeException(ex);
        }

        /* Load static data from configuration */
        blogInfo = new HashMap<String, String>();
        blogInfo.put(TemplateKey.Config.TITLE.toString(), Config.INSTANCE.getTitle());
        blogInfo.put(TemplateKey.Config.AUTHOR.toString(), Config.INSTANCE.getAuthor());
        blogInfo.put(TemplateKey.Config.LANGUAGE.toString(), Config.INSTANCE.getLocale().getLanguage());
    }


    /**
     * Write documents of the list to HTML files. The template for blog posts is used.
     *
     * @param blogposts The list of blog posts
     */
    public void writeBlogPosts(final List<Document> blogposts) {
        final int written = writeDocuments(blogposts, TemplateKey.Prefix.POST, "page_blogpost.ftl");
        LOGGER.info(String.format("%d blog posts written", written));
    }


    /**
     * Write documents of the list to HTML files. The template for simple pages is used.
     *
     * @param pages The list of simple pages
     */
    public void writePages(final List<Document> pages) {
        final int written = writeDocuments(pages, TemplateKey.Prefix.PAGE, "page_page.ftl");
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
    public int writeDocuments(final List<Document> documents, final TemplateKey.Prefix key, final String template) {
        int counter = 0;
        final String keyStr = key.toString();
        final Map<String, Object> content = new HashMap<String, Object>();
        content.put(TemplateKey.Prefix.BLOG.toString(), blogInfo);

        for (final Document document : documents) {
            /* Set the document */
            if (content.containsKey(keyStr)) {
                content.replace(keyStr, document);
                content.replace(TemplateKey.BASEDIR.toString(), document.getToBaseDir());
            } else {
                content.put(keyStr, document);
                content.put(TemplateKey.BASEDIR.toString(), document.getToBaseDir());
            }

            /* The absolute path of the file */
            final Path file = Paths.get(target.toString(), document.getPath());

            try {
                /* Get the parent directory (will be null if file has no parent directory) */
                final Path parent = file.getParent();

                if (parent != null) {
                    /* Create directories */
                    Files.createDirectories(parent);

                    /* Write file to disk using the Freemarker template */
                    if (writeFile(content, file.toFile(), template)) {
                        counter++;
                        LOGGER.info(String.format("Wrote %s", document.getPath()));
                    }
                }

            } catch (IOException ex) {
                LOGGER.severe(String.format("Creating directories failed: %s", ex.getMessage()));
            }
        }

        return counter;
    }


    /**
     * Write index pages for blog posts. For each index page a limited number of blog posts is used.
     *
     * @param blogposts The list of blog posts
     */
    public void writeIndex(final List<Document> blogposts) {
        final Map<String, Object> content = new HashMap<String, Object>();
        content.put(TemplateKey.Prefix.BLOG.toString(), blogInfo);
        content.put(TemplateKey.BASEDIR.toString(), "");
        int counter = 0;

        final int postsPerPage = Config.INSTANCE.getIndexPosts();

        /* Calculate the number of index pages */
        int pages = blogposts.size() / postsPerPage;
        if (blogposts.size() % postsPerPage > 0) {
            pages++;
        }

        /* Create the filenames for pagination */
        String[] filenames = new String[pages + 2];
        filenames[0] = "";
        filenames[1] = String.format("%s.html", Config.INSTANCE.getIndexFile());
        filenames[filenames.length - 1] = "";
        for (int i = 1; i < pages; i++) {
            filenames[i + 1] = String.format("%s-%d.html", Config.INSTANCE.getIndexFile(), i);
        }

        /* Counter for the number of blog posts that were already added to an index page */
        int added = 0;

        /* A list for all blog posts of an index page */
        final List<InnerDocument> posts = new ArrayList<InnerDocument>();

        /* Create all index pages */
        for (int i = 0; i < pages; i++) {
            posts.clear();

            /* Get blog posts for the page */
            for (int j = added; j < (i + 1) * postsPerPage; j++) {
                if (added < blogposts.size()) {
                    posts.add(new InnerDocument(blogposts.get(added)));
                    added++;
                } else {
                    break;
                }
            }

            /* Set the document */
            final String postsKey = TemplateKey.Prefix.POSTS.toString();
            if (content.containsKey(postsKey)) {
                content.replace(postsKey, posts);
                content.replace(TemplateKey.INDEX_NEWER.toString(), filenames[i]);
                content.replace(TemplateKey.INDEX_OLDER.toString(), filenames[i + 2]);
            } else {
                content.put(postsKey, posts);
                content.put(TemplateKey.INDEX_NEWER.toString(), filenames[i]);
                content.put(TemplateKey.INDEX_OLDER.toString(), filenames[i + 2]);
            }

            /* Write file to disk using the Freemarker template */
            if (writeFile(content, new File(target.toFile(), filenames[i + 1]), "page_index.ftl")) {
                counter++;
                LOGGER.info(String.format("Wrote index page %s", filenames[i + 1]));
            }
        }

        LOGGER.info(String.format("%d index pages written", counter));
    }


    /**
     * Write pages for categories. For each category page contains all blog posts of a category.
     *
     * @param blogposts The list of blog posts
     */
    public void writeCategoryPages(final List<Document> blogposts) {
        final Map<String, List<InnerDocument>> categories = new HashMap<String, List<InnerDocument>>();

        /* Build category index */
        for (final Document blogpost : blogposts) {
            for (final Category category : blogpost.getCategories()) {
                categories.putIfAbsent(category.getNameFormatted(), new ArrayList<InnerDocument>());
                categories.get(category.getNameFormatted()).add(new InnerDocument(blogpost));
            }
        }

        /* Write category pages */
        final Map<String, Object> content = new HashMap<String, Object>();
        content.put(TemplateKey.Prefix.BLOG.toString(), blogInfo);
        content.put(TemplateKey.BASEDIR.toString(), "");
        int counter = 0;

        for (final Entry<String, List<InnerDocument>> entry : categories.entrySet()) {
            final String filename = String.format("%s%s.html", Config.INSTANCE.getCategoryFile(),
                    entry.getKey().toLowerCase(Config.INSTANCE.getLocale()));

            /* Set the document */
            if (content.containsKey(TemplateKey.Prefix.POSTS.toString())) {
                content.replace(TemplateKey.Prefix.POSTS.toString(), entry.getValue());
                content.replace(TemplateKey.CATEGORY.toString(), entry.getKey());
            } else {
                content.put(TemplateKey.Prefix.POSTS.toString(), entry.getValue());
                content.put(TemplateKey.CATEGORY.toString(), entry.getKey());
            }

            /* Write file to disk using the Freemarker template */
            if (writeFile(content, new File(target.toFile(), filename), "page_category.ftl")) {
                counter++;
                LOGGER.info(String.format("Wrote category page %s for category %s", filename, entry.getKey()));
            }
        }

        LOGGER.info(String.format("%d category pages written", counter));
    }


    /**
     * Writer that writes out the content of a single document to an HTML file. A template is used to write the file.
     *
     * @param content The content of the document
     * @param file The name of the HTML file
     * @param template The template to use for the HTML file
     * @return Success flag: true = file written successfully, false = error while writing the file
     */
    private boolean writeFile(final Map<String, Object> content, final File file, final String template) {
        try (java.io.Writer out =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            final Template fmTemplate = fmConfig.getTemplate(template);
            fmTemplate.process(content, out);
        } catch (IOException | TemplateException ex) {
            LOGGER.severe(String.format("Error while writing %s: %s", file.getName(), ex.getMessage()));
            return false;
        }
        return true;
    }

}
