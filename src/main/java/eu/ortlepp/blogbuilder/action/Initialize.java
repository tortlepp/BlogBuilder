package eu.ortlepp.blogbuilder.action;

import eu.ortlepp.blogbuilder.model.TemplateFile;
import eu.ortlepp.blogbuilder.util.config.Config;
import eu.ortlepp.blogbuilder.util.config.ConfigItems;
import eu.ortlepp.blogbuilder.util.config.Directories;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Action: Initialize a new project.
 *
 * @author Thorsten Ortlepp
 */
public final class Initialize implements Action {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Initialize.class.getName());

    /** Subdirectories in the "Content" directory. */
    private static final String[] SUBDIRS_CONTENT = {"2016", "2017"};

    /** Subdirectories in the "Resources" directory. */
    private static final String[] SUBDIRS_RESOURCES = {"images"};

    /** The directory of the project to initialize. */
    private final Path directory;


    /**
     * Constructor, prepares the initialization.
     *
     * @param directory Directory of the project to initialize
     */
    public Initialize(final String directory) {
        this.directory = Paths.get(directory);
    }


    /**
     * Run the initialization process step by step.
     */
    @Override
    public void run() {
        if (Files.exists(directory)) {
            LOGGER.severe(String.format("Directory %s already exists, initialization aborted",
                    directory.getFileName()));

        } else {
            LOGGER.info(String.format("Starting initialization for %s", directory.getFileName()));

            try {
                createDirectories();
                copyFiles();
                LOGGER.info("Initialization successful");
            } catch (IOException ex) {
                LOGGER.severe(String.format("Initialization failed: %s", ex.getMessage()));
            }
        }
    }


    /**
     * Create all directories and subdirectories.
     *
     * @throws IOException Error while creating the directories
     */
    private void createDirectories() throws IOException {
        final String dirStr = directory.toString();

        /* Project root */
        Files.createDirectories(directory);
        LOGGER.info(String.format("Created project directory %s", directory.toString()));

        /* Main directories */
        Files.createDirectory(Paths.get(dirStr, Directories.BLOG.toString()));
        Files.createDirectory(Paths.get(dirStr, Directories.CONTENT.toString()));
        Files.createDirectory(Paths.get(dirStr, Directories.RESOURCES.toString()));
        Files.createDirectory(Paths.get(dirStr, Directories.TEMPLATES.toString()));
        LOGGER.info("Created main directories");

        /* Subdirectories for content and resources */
        for (final String subdir : SUBDIRS_CONTENT) {
            Files.createDirectory(Paths.get(dirStr, Directories.CONTENT.toString(), subdir));
        }
        for (final String subdir : SUBDIRS_RESOURCES) {
            Files.createDirectory(Paths.get(dirStr, Directories.RESOURCES.toString(), subdir));
        }
        LOGGER.info("Created subdirectories");
    }


    /**
     * Copy sample files from the JAR archive into the created directories.
     * The properties file is not copied but created from scratch.
     *
     * @throws IOException Error while copying / creating the files
     */
    private void copyFiles() throws IOException {
        final String dirStr = directory.toString();

        /* Write configuration / properties file */
        writeConfiguration(new File(dirStr, Config.CONFIG_FILE));
        LOGGER.info("Created configuration file");

        /* Copy content / Markdown files */
        copyContentFiles(dirStr);
        LOGGER.info("Created sample Markdown files");

        /* Copy resource files */
        copyResourceFiles(dirStr);
        LOGGER.info("Created sample resource files");

        /* Copy template files */
        copyTemplateFiles(dirStr);
        LOGGER.info("Created Freemarker templates");
    }


    /**
     * Write all configuration items to a properties file. For each item a description is added.
     *
     * @param file The name (and path) of the properties file
     * @throws IOException Error while writing the properties file
     */
    private void writeConfiguration(final File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.ISO_8859_1))) {

            /* Write all configuration items */
            for (final ConfigItems item : ConfigItems.values()) {
                writer.append("# ").append(item.getDescription());
                writer.newLine();
                writer.append(item.toString());
                writer.newLine();
                writer.newLine();
            }

        } catch (IOException ex) {
            LOGGER.severe(String.format("Error while writing configuration file %s: %s",
                    file.getName(), ex.getMessage()));
            throw ex;
        }
    }


    /**
     * Copy all content (Markdown) files from the JAR file to the project folder.
     *
     * @param directory The directory of the project
     * @throws IOException Error while copying the files
     */
    private void copyContentFiles(final String directory) throws IOException {
        final String dirStrContent = Paths.get(directory, Directories.CONTENT.toString()).toString();
        final String dirStrYear1 = Paths.get(dirStrContent, SUBDIRS_CONTENT[0]).toString();
        final String dirStrYear2 = Paths.get(dirStrContent, SUBDIRS_CONTENT[1]).toString();

        try {
            Files.copy(getResourceStream("content/cupcake_ipsum_part_i.md"),
                    Paths.get(dirStrContent, "cupcake_ipsum_part_i.md"));
            Files.copy(getResourceStream("content/cupcake_ipsum_part_ii.md"),
                    Paths.get(dirStrContent, "cupcake_ipsum_part_ii.md"));
            Files.copy(getResourceStream("content/chocolate_bar.md"),
                    Paths.get(dirStrYear1, "chocolate_bar.md"));
            Files.copy(getResourceStream("content/halvah_pastry.md"),
                    Paths.get(dirStrYear1, "halvah_pastry.md"));
            Files.copy(getResourceStream("content/fruitcake_jelly_topping.md"),
                    Paths.get(dirStrYear2, "fruitcake_jelly_topping.md"));
            Files.copy(getResourceStream("content/jelly_bear.md"),
                    Paths.get(dirStrYear2, "jelly_bear.md"));
            Files.copy(getResourceStream("content/oat_cake.md"),
                    Paths.get(dirStrYear2, "oat_cake.md"));
            Files.copy(getResourceStream("content/pudding_cake.md"),
                    Paths.get(dirStrYear2, "pudding_cake.md"));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Error while copying content files: %s", ex.getMessage()));
            throw ex;
        }
    }


    /**
     * Copy all resource files (images, css, ...) from the JAR file to the project folder.
     *
     * @param directory The directory of the project
     * @throws IOException Error while copying the files
     */
    private void copyResourceFiles(final String directory) throws IOException {
        final String dirStrResources = Paths.get(directory, Directories.RESOURCES.toString()).toString();

        try {
            Files.copy(getResourceStream("resources/image.jpg"),
                    Paths.get(dirStrResources, SUBDIRS_RESOURCES[0], "image.jpg"));
            Files.copy(getResourceStream("resources/style.css"),
                    Paths.get(dirStrResources, "style.css"));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Error while copying resource files: %s", ex.getMessage()));
            throw ex;
        }
    }


    /**
     * Copy all template files from the JAR file to the project folder.
     *
     * @param directory The directory of the project
     * @throws IOException Error while copying the files
     */
    private void copyTemplateFiles(final String directory) throws IOException {
        final String dirStrTemplates = Paths.get(directory, Directories.TEMPLATES.toString()).toString();

        try {
            Files.copy(getResourceStream("templates/include_footer.ftl"),
                    Paths.get(dirStrTemplates, "include_footer.ftl"));
            Files.copy(getResourceStream("templates/include_header.ftl"),
                    Paths.get(dirStrTemplates, "include_header.ftl"));
            Files.copy(getResourceStream(TemplateFile.BLOGPOST.getResourcePath()),
                    Paths.get(dirStrTemplates, TemplateFile.BLOGPOST.toString()));
            Files.copy(getResourceStream(TemplateFile.CATEGORY.getResourcePath()),
                    Paths.get(dirStrTemplates, TemplateFile.CATEGORY.toString()));
            Files.copy(getResourceStream(TemplateFile.INDEX.getResourcePath()),
                    Paths.get(dirStrTemplates, TemplateFile.INDEX.toString()));
            Files.copy(getResourceStream(TemplateFile.PAGE.getResourcePath()),
                    Paths.get(dirStrTemplates, TemplateFile.PAGE.toString()));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Error while copying template files: %s", ex.getMessage()));
            throw ex;
        }
    }


    /**
     * Get a resource file from the JAR archive.
     *
     * @param resource The resource file to get
     * @return The resource file as stream
     */
    private InputStream getResourceStream(final String resource) {
        return this.getClass().getClassLoader().getResourceAsStream("eu/ortlepp/blogbuilder/samples/" + resource);
    }

}
