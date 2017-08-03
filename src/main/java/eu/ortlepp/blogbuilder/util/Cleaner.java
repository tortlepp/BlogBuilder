package eu.ortlepp.blogbuilder.util;

import eu.ortlepp.blogbuilder.util.config.Config;
import eu.ortlepp.blogbuilder.util.config.Directories;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

/**
 * A tool class to delete the contents (files and directories) of the "Blog" directory.
 *
 * @author Thorsten Ortlepp
 */
public final class Cleaner extends SimpleFileVisitor<Path> {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(Cleaner.class.getName());

    /** The parent / start directory whose content is deleted. */
    private final Path startdir;

    /** A list of files that are ignored (= not deleted). */
    private final String[] ignore;


    /**
     * Constructor, initializes the cleaning process.
     *
     * @param directory The project directory whose "Blog" directory should be cleaned
     */
    public Cleaner(final String directory) {
        super();
        startdir = Paths.get(directory, Directories.BLOG.toString());
        ignore = Config.INSTANCE.getCleanIgnore();
    }


    /**
     * Do the cleaning: delete all files and directories from the directory set in the constructor
     * (except those who are in the ignore list). The directory itself is not deleted.
     */
    public void clean() {
        try {
            Files.walkFileTree(startdir, this);
            LOGGER.info(String.format("Cleaned directory %s", startdir.getFileName()));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Cleaning directory %s failed: %s", startdir.getFileName(), ex.getMessage()));
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
        if (!isOnIgnoreList(file.getFileName())) {
            Files.deleteIfExists(file);
        }
        return FileVisitResult.CONTINUE;
    }


    /**
     * After visiting a directory: Delete the directory (except the parent / start directory) if it
     * is empty. If the directory is not empty, it will not be deleted.
     *
     * @param directory The visited directory itself
     * @param exception Error while visiting the files inside the directory
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while deleting the directory
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path directory, final IOException exception) throws IOException {
        /* Necessary because directory.toFile().list() could return null */
        final String[] files = directory.toFile().list();

        if (!directory.equals(startdir) && files != null && files.length == 0) {
            Files.deleteIfExists(directory);
        }
        return FileVisitResult.CONTINUE;
    }


    /**
     * Checks, if a filename is on the ignore list. If the filename is null, true = "on ignore list"
     * is returned because the file cannot be deleted anyway.
     *
     * @param filename The filename to check
     * @return The result of the check; true = the filename is on the ignore list (or was null),
     *  false = the filename is not in the ignore list
     */
    private boolean isOnIgnoreList(final Path filename) {
        if (filename != null) {
            for (final String name : ignore) {
                if (name.equals(filename.toString())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
