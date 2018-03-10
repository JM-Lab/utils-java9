package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPath;
import kr.jm.utils.helper.JMResources;

import java.io.File;
import java.util.List;

/**
 * The interface String list submission publisher interface.
 */
public interface StringListSubmissionPublisherInterface extends
        JMSubmissionPublisherInterface<List<String>> {

    /**
     * Submit file path int.
     *
     * @param filePath the file path
     * @return the int
     */
    default int submitFilePath(String filePath) {
        return submitFile(JMPath.getPath(filePath).toFile());
    }

    /**
     * Submit file int.
     *
     * @param file the file
     * @return the int
     */
    default int submitFile(File file) {
        return submit(JMFiles.readLines(file));
    }

    /**
     * Submit classpath int.
     *
     * @param resourceClasspath the resource classpath
     * @return the int
     */
    default int submitClasspath(String resourceClasspath) {
        return submit(JMResources.readLines(resourceClasspath));
    }

    /**
     * Submit file path or classpath int.
     *
     * @param filePathOrResourceClasspath the file path or resource classpath
     * @return the int
     */
    default int submitFilePathOrClasspath(
            String filePathOrResourceClasspath) {
        return submit(JMOptional
                .getOptional(JMFiles.readLines(filePathOrResourceClasspath))
                .orElseGet(() -> JMResources
                        .readLines(filePathOrResourceClasspath)));
    }

}
