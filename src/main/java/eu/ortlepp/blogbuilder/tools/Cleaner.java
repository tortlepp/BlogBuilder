package eu.ortlepp.blogbuilder.tools;

import eu.ortlepp.blogbuilder.tools.config.Config;

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

    /** A list of files that are ignored (= not deleted). */
    private static String[] ignore;


    /**
     * Do the cleaning: delete all files and directories from the directory. The directory itself is not deleted.
     *
     * @param directory The directory whose contents should be deleted
     */
    public static void clean(final Path directory) {
        startdir = directory;
        ignore = Config.INSTANCE.getCleanIgnore();
        try {
            Files.walkFileTree(directory, new Cleaner());
            LOGGER.info(String.format("Cleaned directory %s", directory.getFileName()));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Cleaning directory %s failed: %s", directory.getFileName(), ex.getMessage()));
            throw new RuntimeException(ex);
        }
    }


    /**
     * Visiting a file: Delete the file if it is not on the ignore list.
     *
     * @param file The visited file itself
     * @param attrs The attributes of the file
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while deleting the file
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (!isIgnored(file.getFileName().toString())) {
            Files.deleteIfExists(file);
        }
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
    public FileVisitResult postVisitDirectory(final Path directory, final IOException exception) throws IOException {
        if (!directory.equals(startdir) && directory.toFile().list().length == 0) {
            Files.deleteIfExists(directory);
        }
        return FileVisitResult.CONTINUE;
    }


    /**
     * Checks, if a file is on the ignore list.
     *
     * @param filename The filename to check
     * @return The result of the check; true = the file is on the ignore list,
     *  false = the file is not in the ignore list
     */
    private boolean isIgnored(String filename) {
        for (final String name : ignore) {
            if (name.equals(filename)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Private constructor for tool class - without any functionality.
     */
    private Cleaner() {
        /* Nothing happens here... */
    }

}
