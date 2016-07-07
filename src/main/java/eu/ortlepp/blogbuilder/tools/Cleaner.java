package eu.ortlepp.blogbuilder.tools;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

/**
 * A tool class to delete the contents (files and child directories) of a directory.
 *
 * @author Torsten Ortlepp
 */
public final class Cleaner extends SimpleFileVisitor<Path> {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Cleaner.class.getName());

    /** The parent / start directory which must not be deleted. */
    private static Path startdir;


    /**
     * Do the cleaning: delete all files and directories from the directory.
     * The directory itself is not deleted.
     *
     * @param directory The directory whose contents should be deleted
     */
    public static void clean(Path directory) {
        startdir = directory;
        try {
            Files.walkFileTree(directory, new Cleaner());
            LOGGER.info(String.format("Cleaned directory %s", directory.getFileName()));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Cleaning directory %s failed: %s", directory.getFileName(), ex.getMessage()));
            throw new RuntimeException(ex);
        }
    }


    /**
     * Visiting a file: Delete the file.
     *
     * @param file The visited file itself
     * @param attrs The attributes of the file
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while deleting the file
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.deleteIfExists(file);
        return FileVisitResult.CONTINUE;
    }


    /**
     * After visiting a directory: Delete the directory (except the parent / start directory).
     *
     * @param directory The visited directory itself
     * @param exception Error while visiting the files inside the directory
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while deleting the directory
     */
    @Override
    public FileVisitResult postVisitDirectory(Path directory, IOException exception) throws IOException {
        if(!directory.equals(startdir)) {
            Files.deleteIfExists(directory);
        }
        return FileVisitResult.CONTINUE;
    }


    /**
     * Private constructor for tool class - without any functionality.
     */
    private Cleaner() {
        /* Nothing happens here... */
    }

}
