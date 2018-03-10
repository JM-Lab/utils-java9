package kr.jm.utils.helper;

import kr.jm.utils.HttpGetRequester;
import kr.jm.utils.datastructure.JMCollections;

import java.util.List;

/**
 * The type Jm restful resource.
 */
public class JMRestfulResource {
    private static final String HTTP = "http";

    /**
     * Gets string with rest or classpath or file path.
     *
     * @param resourceWithRestOrClasspathOrFilePath the resource with rest or classpath or file path
     * @param charsetName                           the charset name
     * @return the string with rest or classpath or file path
     */
    public static String getStringWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath, String charsetName) {
        return resourceWithRestOrClasspathOrFilePath.startsWith(HTTP)
                ? HttpGetRequester.getResponseAsString(
                resourceWithRestOrClasspathOrFilePath, charsetName)
                : JMResources.getStringWithClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath, charsetName);
    }

    /**
     * Gets string with rest or classpath or file path.
     *
     * @param resourceWithRestOrClasspathOrFilePath the resource with rest or classpath or file path
     * @return the string with rest or classpath or file path
     */
    public static String getStringWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath) {
        return getStringWithRestOrClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath,
                JMResources.UTF_8_CharsetString);
    }

    /**
     * Read lines with rest or classpath or file path list.
     *
     * @param resourceWithRestOrClasspathOrFilePath the resource with rest or classpath or file path
     * @param charsetName                           the charset name
     * @return the list
     */
    public static List<String> readLinesWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath, String charsetName) {
        return JMCollections
                .buildListByLine(getStringWithRestOrClasspathOrFilePath(
                        resourceWithRestOrClasspathOrFilePath, charsetName));
    }

    /**
     * Read lines with rest or classpath or file path list.
     *
     * @param resourceWithRestOrClasspathOrFilePath the resource with rest or classpath or file path
     * @return the list
     */
    public static List<String> readLinesWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath) {
        return readLinesWithRestOrClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath,
                JMResources.UTF_8_CharsetString);
    }

    /**
     * Gets string with rest or file path or classpath.
     *
     * @param resourceWithRestOrFilePathOrClasspath the resource with rest or file path or classpath
     * @param charsetName                           the charset name
     * @return the string with rest or file path or classpath
     */
    public static String getStringWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath, String charsetName) {
        return resourceWithRestOrFilePathOrClasspath.startsWith(HTTP)
                ? HttpGetRequester.getResponseAsString(
                resourceWithRestOrFilePathOrClasspath, charsetName)
                : JMResources.getStringWithFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath, charsetName);
    }

    /**
     * Gets string with rest or file path or classpath.
     *
     * @param resourceWithRestOrFilePathOrClasspath the resource with rest or file path or classpath
     * @return the string with rest or file path or classpath
     */
    public static String getStringWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath) {
        return getStringWithRestOrFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath,
                JMResources.UTF_8_CharsetString);
    }

    /**
     * Read lines with rest or file path or classpath list.
     *
     * @param resourceWithRestOrFilePathOrClasspath the resource with rest or file path or classpath
     * @param charsetName                           the charset name
     * @return the list
     */
    public static List<String> readLinesWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath, String charsetName) {
        return JMCollections
                .buildListByLine(getStringWithRestOrClasspathOrFilePath(
                        resourceWithRestOrFilePathOrClasspath, charsetName));
    }

    /**
     * Read lines with rest or file path or classpath list.
     *
     * @param resourceWithRestOrFilePathOrClasspath the resource with rest or file path or classpath
     * @return the list
     */
    public static List<String> readLinesWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath) {
        return readLinesWithRestOrFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath,
                JMResources.UTF_8_CharsetString);
    }
}
