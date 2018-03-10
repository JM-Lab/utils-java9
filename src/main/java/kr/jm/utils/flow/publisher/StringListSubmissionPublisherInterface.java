package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMPath;
import kr.jm.utils.helper.JMResources;

import java.io.File;
import java.util.List;

public interface StringListSubmissionPublisherInterface extends
        JMSubmissionPublisherInterface<List<String>> {

    default int submitFilePath(String filePath) {
        return submitFile(JMPath.getPath(filePath).toFile());
    }

    default int submitFile(File file) {
        return submit(JMFiles.readLines(file));
    }

    default int submitClasspath(String resourceClasspath) {
        return submit(JMResources.readLines(resourceClasspath));
    }

    default int submitFilePathOrClasspath(
            String filePathOrResourceClasspath) {
        return submit(JMOptional
                .getOptional(JMFiles.readLines(filePathOrResourceClasspath))
                .orElseGet(() -> JMResources
                        .readLines(filePathOrResourceClasspath)));
    }

}
