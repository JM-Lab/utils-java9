package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPath;
import kr.jm.utils.helper.JMResources;

import java.io.File;
import java.util.stream.Stream;

/**
 * The type String list submission publisher.
 */
public class LineSubmissionPublisher extends
        JMSubmissionPublisher<String> {
    /**
     * Submit file path int.
     *
     * @param filePath the file path
     * @return the int
     */
    public int submitFilePath(String filePath) {
        return submitFile(JMPath.getPath(filePath).toFile());
    }

    /**
     * Submit file int.
     *
     * @param file the file
     * @return the int
     */
    public int submitFile(File file) {
        return submitStream(JMFiles.readLines(file).stream());

    }

    public int submitStream(Stream<String> stream) {
        return stream.mapToInt(this::submit).sum();
    }


    /**
     * Submit classpath int.
     *
     * @param resourceClasspath the resource classpath
     * @return the int
     */
    public int submitClasspath(String resourceClasspath) {
        return submitStream(JMResources.readLines(resourceClasspath).stream());
    }

    /**
     * Submit file path or classpath int.
     *
     * @param filePathOrResourceClasspath the file path or resource classpath
     * @return the int
     */
    public int submitFilePathOrClasspath(
            String filePathOrResourceClasspath) {
        return submitStream(JMOptional
                .getOptional(JMFiles.readLines(filePathOrResourceClasspath))
                .orElseGet(() -> JMResources
                        .readLines(filePathOrResourceClasspath)).stream());
    }
}
