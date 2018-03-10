package kr.jm.utils.helper;

import kr.jm.utils.HttpGetRequester;
import kr.jm.utils.datastructure.JMCollections;

import java.util.List;

public class JMRestfulResource {
    private static final String HTTP = "http";

    public static String getStringWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath, String charsetName) {
        return resourceWithRestOrClasspathOrFilePath.startsWith(HTTP)
                ? HttpGetRequester.getResponseAsString(
                resourceWithRestOrClasspathOrFilePath, charsetName)
                : JMResources.getStringWithClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath, charsetName);
    }

    public static String getStringWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath) {
        return getStringWithRestOrClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath,
                JMResources.UTF_8_CharsetString);
    }

    public static List<String> readLinesWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath, String charsetName) {
        return JMCollections
                .buildListByLine(getStringWithRestOrClasspathOrFilePath(
                        resourceWithRestOrClasspathOrFilePath, charsetName));
    }

    public static List<String> readLinesWithRestOrClasspathOrFilePath(
            String resourceWithRestOrClasspathOrFilePath) {
        return readLinesWithRestOrClasspathOrFilePath(
                resourceWithRestOrClasspathOrFilePath,
                JMResources.UTF_8_CharsetString);
    }

    public static String getStringWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath, String charsetName) {
        return resourceWithRestOrFilePathOrClasspath.startsWith(HTTP)
                ? HttpGetRequester.getResponseAsString(
                resourceWithRestOrFilePathOrClasspath, charsetName)
                : JMResources.getStringWithFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath, charsetName);
    }

    public static String getStringWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath) {
        return getStringWithRestOrFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath,
                JMResources.UTF_8_CharsetString);
    }

    public static List<String> readLinesWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath, String charsetName) {
        return JMCollections
                .buildListByLine(getStringWithRestOrClasspathOrFilePath(
                        resourceWithRestOrFilePathOrClasspath, charsetName));
    }

    public static List<String> readLinesWithRestOrFilePathOrClasspath(
            String resourceWithRestOrFilePathOrClasspath) {
        return readLinesWithRestOrFilePathOrClasspath(
                resourceWithRestOrFilePathOrClasspath,
                JMResources.UTF_8_CharsetString);
    }
}
