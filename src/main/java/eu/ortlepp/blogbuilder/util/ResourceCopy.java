package eu.ortlepp.blogbuilder.util;

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
 * A tool class to copy the contents from the resources directory to the directory with the built blog.
 *
 * @author Thorsten Ortlepp
 */
public final class ResourceCopy extends SimpleFileVisitor<Path> {

    /** A logger to write out messages to the user. */
    private static final Logger LOGGER = Logger.getLogger(ResourceCopy.class.getName());

    /** The source directory (where the files are copied from). */
    private final Path source;

    /** The target directory for the built blog (where the files are copied to). */
    private final Path target;

    /** A counter for all successfully copied files. */
    private int counter;


    /**
     * Constructor, initializes the copy process.
     *
     * @param directory The project directory which contains the resources and the target directory
     */
    public ResourceCopy(final String directory) {
        super();
        source = Paths.get(directory, Directories.RESOURCES.toString());
        target = Paths.get(directory, Directories.BLOG.toString());
        counter = 0;
    }


    /**
     * Do the copying: Copy all files from the resources directory to the target directory for built blogs. If
     * necessary copy the directory structure as well. If a file already exists in the target directory it is
     * skipped.
     */
    public void copyResources() {
        try {
            Files.walkFileTree(source, this);
            LOGGER.info(String.format("%d resource files copied", counter));
        } catch (IOException ex) {
            LOGGER.severe(String.format("Error while copying resource files: %s", ex.getMessage()));
            throw new RuntimeException(ex);
        }
    }


    /**
     * Visiting a file: Copy the file from the resources directory to the target directory for the built blog.
     *
     * @param file The visited file itself
     * @param attrs The attributes of the file
     * @return The result of the visit: Continue to visit other files and directories
     * @throws IOException Error while copying the file
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        /* Create full target path */
        Path temp = target.relativize(file);
        temp = temp.subpath(2, temp.getNameCount());
        temp = Paths.get(target.toString(), temp.toString());

        if (Files.exists(temp)) {
            LOGGER.warning(String.format("Resource file %s already exists, file not copied",
                    Tools.getFilenameFromPath(temp)));
        } else {

            /* Check parent because it could be null when the path does not contain a parent */
            final Path tmpParent = temp.getParent();
            if (tmpParent != null) {
                /* Create folders and copy file */
                Files.createDirectories(tmpParent);
                Files.copy(file, temp);
                counter++;
                LOGGER.info(String.format("Resource file %s copied", Tools.getFilenameFromPath(temp)));
            }
        }

        return FileVisitResult.CONTINUE;
    }

}
