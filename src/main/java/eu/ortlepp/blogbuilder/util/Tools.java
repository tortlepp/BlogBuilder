package eu.ortlepp.blogbuilder.util;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.ortlepp.blogbuilder.util.config.Config;

/**
 * An utility class with some useful (static) methods.
 *
 * @author Thorsten Ortlepp
 */
public final class Tools {

    /**
     * Change all non-absolute links in an HTML formatted string into relative links. All links in href and src
     * attributes are prepended with the relative path to the base directory (e.g. ../../path/file.html).
     *
     * @param content The (HTML) text in which the links should be changed
     * @param relative The relative path to add to the links
     * @return The content with changed links
     */
    public static String makeLinksRelative(final String content, final String relative) {
        String replaced = replaceLinks(content, relative, "href");
        replaced = replaceLinks(replaced, relative, "src");
        return replaced;
    }


    /**
     * Change all non-absolute links in an HTML formatted string into absolute links. All links in href and src
     * attributes are prepended with the base URL.
     *
     * @param content The (HTML) text in which the links should be changed
     * @return The content with changed links
     */
    public static String makeLinksAbsolute(final String content) {
        String baseurl = Config.INSTANCE.getBaseUrl();
        if (!baseurl.endsWith("/")) {
            baseurl += "/";
        }

        String replaced = replaceLinks(content, baseurl, "href");
        replaced = replaceLinks(replaced, baseurl, "src");
        return replaced;
    }


    /**
     * Adds a prefix to all relative links (links that do not start with  http / https).
     *
     * @param content The (HTML) text in which the links should be changed
     * @param prefix The prefix to add to all relative links
     * @param attribute The attribute whose links are changed
     * @return The content with changed links
     */
    private static String replaceLinks(final String content, final String prefix, final String attribute) {
        final Matcher matcher = Pattern.compile(attribute + "=\".*?\"").matcher(content);
        final StringBuffer strBuffer = new StringBuffer(content.length());

        while (matcher.find()) {
            String found = matcher.group();

            /* Change all links that do not start with http / https */
            if (!found.startsWith(attribute + "=\"http:") && !found.startsWith(attribute + "=\"https:")) {
                found = found.replace(attribute + "=\"", attribute + "=\"" + prefix);
                matcher.appendReplacement(strBuffer, Matcher.quoteReplacement(found));
            }
        }

        matcher.appendTail(strBuffer);
        return strBuffer.toString();
    }


    /**
     * Extract the filename from a path.
     *
     * @param path The complete path
     * @return The extracted filename; or "unknown" if path is not valid
     */
    public static String getFilenameFromPath(final Path path) {
        final Optional<Path> optional = Optional.ofNullable(path.getFileName());
        return optional.isPresent() ? optional.get().toString() : "unknown";
    }


    /**
     * Private constructor for tool class - without any functionality.
     */
    private Tools() {
        /* Nothing happens here... */
    }

}
