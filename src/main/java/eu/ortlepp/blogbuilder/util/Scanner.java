package eu.ortlepp.blogbuilder.util;

import eu.ortlepp.blogbuilder.model.Document;
import eu.ortlepp.blogbuilder.model.DocumentType;
import eu.ortlepp.blogbuilder.util.config.Directories;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * A scanner for the "Content" directory. Searches recursively for Markdown files and reads their content.
 * Only files with the extension .md are read, files with other extensions are ignored.
 *
 * @author Thorsten Ortlepp
 */
public final class Scanner extends SimpleFileVisitor<Path> {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Scanner.class.getName());

    /** Pattern for parsing date and time. */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd H:mm";

    /** List of scanned documents / files; contains only valid documents. */
    private final List<Document> files;

    /** Date and time format to parse date and time headers of the Markdown files. */
    private final DateTimeFormatter inputFormat;

    /** The directory where the Markdown files are located. Necessary to create relative paths. */
    private final Path dirContent;


    /**
     * Constructor, initializes the scanner.
     *
     * @param directory The project directory whose "Content" directory should be read
     */
    public Scanner(final String directory) {
        dirContent = Paths.get(directory, Directories.CONTENT.toString());
        files = new ArrayList<Document>();
        inputFormat = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    }


    /**
     * Scan the "Content" directory recursively and find all Markdown files. The blog posts and simple pages
     * that were found are added to the result list.
     *
     * @return A list of all found Markdown files and their content
     */
    public List<Document> scanDirectory() {
        files.clear();
        try {
            Files.walkFileTree(dirContent, this);
        } catch (IOException ex) {
            LOGGER.severe(String.format("Scanning %s failed: %s", dirContent.getFileName(), ex.getMessage()));
            throw new RuntimeException(ex);
        }
        return files;
    }


    /**
     * Visiting a file: If the file extension is .md the file is read. If the file is valid it is added to the list of
     * read files. After visiting a file visit the next file.
     *
     * @param file The visited file itself
     * @param attrs The attributes of the file
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while reading the file
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

        if (file.toString().endsWith(".md")) {
            final Document document = readFile(file);

            if (document.isValidDocument()) {
                files.add(document);
                LOGGER.info(String.format("Found %s (%s)", Tools.getFilenameFromPath(file), document.getTitle()));
            }
        }

        return FileVisitResult.CONTINUE;
    }


    /**
     * Read a Markdown file and put its content into a Document object.
     *
     * @param file The file to read
     * @return The content of the file as Document object
     */
    private Document readFile(final Path file) {
        final Document document = createDocument(file);

        try {
            /* Read the file */
            final List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            /* Create Document data object */
            parseContentFile(lines, document);

        } catch (IOException ex) {
            LOGGER.severe(String.format("Reading %s failed: %s", Tools.getFilenameFromPath(file), ex.getMessage()));
        }

        return document;
    }


    /**
     * Create a Document data object and initialize it with the correct files and paths.
     *
     * @param file The content / Markdown file that is the base for the Document data object
     * @return The created and initialized Document data object
     */
    private Document createDocument(final Path file) {
        /* Get the relative path of the file and change the extension to .html */
        String htmlFile = dirContent.relativize(file).toString();
        htmlFile = htmlFile.replaceAll("\\\\", "/");
        htmlFile = htmlFile.substring(0, htmlFile.lastIndexOf('.')) + ".html";

        /* Build the relative path to the base directory */
        String toBaseDir = file.relativize(dirContent).toString();
        toBaseDir = toBaseDir.replaceAll("\\\\", "/");
        if ("..".equals(toBaseDir)) {
            toBaseDir = "";
        } else {
            toBaseDir = toBaseDir.replaceFirst("../", "") + "/";
        }

        return new Document(file, htmlFile, toBaseDir);
    }


    /**
     * Parse the content lines of a Markdown file and add the read content of the file to a Document data object.
     *
     * @param lines The content lines of the Markdown file
     * @param document The Document data object that is related to the file
     */
    private void parseContentFile(final List<String> lines, final Document document) {
        for (final String line : lines) {

            /* Each header line starts with ;; */
            if (line.trim().startsWith(";;")) {

                /* Split line to a key-value-pair */
                final String[] keyvalue = line.trim().substring(2).split("=");

                /* Set headers */
                switch (keyvalue[0].trim().toLowerCase(Locale.getDefault())) {
                    case "title":
                        document.setTitle(keyvalue[1].trim());
                        break;
                    case "created":
                        document.setCreated(parseString(keyvalue[1].trim()));
                        break;
                    case "modified":
                        document.setModified(parseString(keyvalue[1].trim()));
                        break;
                    case "noblog":
                        document.setType(DocumentType.PAGE);
                        break;
                    case "category":
                        final String[] categories = keyvalue[1].split(",");
                        for (final String category : categories) {
                            document.addCategory(category);
                        }
                        break;
                    default:
                        LOGGER.warning(String.format("Unknown header %s in %s", keyvalue[0],
                                Tools.getFilenameFromPath(document.getFile())));
                        break;
                }

            } else {
                /* Everything that is not a header is treated as content */
                document.addContent(line + System.lineSeparator());
            }
        }
    }


    /**
     * Converts a string into a date and time object. If the string is formatted in a wrong way the minimum date
     * is returned.
     *
     * @param string The string to parse
     * @return The date and time object
     */
    private LocalDateTime parseString(final String string) {
        try {
            return LocalDateTime.parse(string, inputFormat);
        } catch (DateTimeException ex) {
            LOGGER.warning(String.format("%s does not match %s", string, DATE_TIME_PATTERN));
            return LocalDateTime.MIN;
        }
    }

}
