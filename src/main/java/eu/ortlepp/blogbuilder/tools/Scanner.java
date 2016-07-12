package eu.ortlepp.blogbuilder.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import eu.ortlepp.blogbuilder.model.Document;

/**
 * A scanner for the content directory. Searches recursively for Markdown files and reads their content.
 * Only files with the extension .md are read, files with other extensions are ignored.
 *
 * @author Thorsten Ortlepp
 */
public class Scanner extends SimpleFileVisitor<Path> {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Scanner.class.getName());

    /** Pattern for parsing date and time. */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd H:mm";

    /** List of scanned documents / files; contains only valid documents. */
    private final List<Document> files;

    /** Date and time format to parse date and time headers of the Markdown files. */
    private final DateTimeFormatter inputFormat;

    /** The directory where the Markdown files are located. Necessary to create relative paths. */
    private Path dirContent;


    /**
     * Constructor, initializes the scanner.
     */
    public Scanner() {
        files = new ArrayList<Document>();
        inputFormat = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    }


    /**
     * Scan the directory recursively and find all Markdown files.
     * The blog posts and simple pages that were found are added to the result list.
     *
     * @param directory The directory to scan
     * @return A list of all found Markdown files and their content
     */
    public List<Document> scanDirectory(Path directory) {
        dirContent = directory;
        files.clear();
        try {
            Files.walkFileTree(directory, this);
        } catch (IOException ex) {
            LOGGER.severe(String.format("Scanning %s failed: %s", directory.getFileName(), ex.getMessage()));
            throw new RuntimeException(ex);
        }
        return files;
    }


    /**
     * Visiting a file: If the file extension is .md the file is read. If the file is valid it is added
     * to the list of read files. After visiting a file visit the next file.
     *
     * @param file The visited file itself
     * @param attrs The attributes of the file
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while reading the file
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (file.toString().endsWith(".md")) {
            Document document = readFile(file);

            if (document.isValidDocument()) {
                files.add(document);
                LOGGER.info(String.format("Found %s (%s)", file.getFileName().toString(), document.getTitle()));
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
    private Document readFile(Path file) {
        /* Get the relative path of the file and change the extension to .html */
        String htmlFile = dirContent.relativize(file).toString();
        htmlFile = htmlFile.replaceAll("\\\\", "/");
        htmlFile = htmlFile.substring(0, htmlFile.lastIndexOf('.')) + ".html";

        /* Build the relative path to the base directory */
        String toBaseDir = file.relativize(dirContent).toString();
        toBaseDir = toBaseDir.replaceAll("\\\\", "/");
        if (toBaseDir.equals("..")) {
            toBaseDir = "";
        } else {
            toBaseDir = toBaseDir.replaceFirst("../", "") + "/";
        }

        Document document = new Document(file, htmlFile, toBaseDir);

        try {
            /* Read the file */
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            for (String line : lines) {

                /* Headers start with ;; */
                if (line.trim().startsWith(";;")) {

                    /* Split line to a key-value-pair */
                    String[] keyvalue = line.trim().substring(2).split("=");

                    /* Set headers */
                    switch(keyvalue[0].trim().toLowerCase(Locale.getDefault())) {
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
                            document.setNoBlog();
                            break;
                        default:
                            LOGGER.warning(String.format("Unknown header %s in %s", keyvalue[0], file.getFileName().toString()));
                            break;
                    }
                }

                /* Everything that is not a header is handled as content */
                else {
                    document.addContent(line + System.lineSeparator());
                }
            }

        } catch (IOException ex) {
            LOGGER.severe(String.format("Reading %s failed: %s", file.getFileName().toString(), ex.getMessage()));
        }

        return document;
    }


    /**
     * Converts a string into a date and time object. If the string is formatted in a wrong way the minimum
     * date is returned.
     *
     * @param string The string to parse
     * @return The date and time object
     */
    private LocalDateTime parseString(String string) {
        try {
            return LocalDateTime.parse(string, inputFormat);
        } catch (DateTimeException ex) {
            LOGGER.warning(String.format("%s does not match %s", string, DATE_TIME_PATTERN));
            return LocalDateTime.MIN;
        }
    }

}
