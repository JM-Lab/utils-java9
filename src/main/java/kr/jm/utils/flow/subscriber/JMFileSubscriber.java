package kr.jm.utils.flow.subscriber;

import kr.jm.utils.JMFileAppender;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * The type File output.
 */

public class JMFileSubscriber<I> extends JMSubscriber<I> implements
        AutoCloseable {

    private JMFileAppender fileAppender;

    public JMFileSubscriber(String filePath) {
        this(filePath, false);
    }

    public JMFileSubscriber(String filePath, boolean enableJsonString) {
        this(filePath,
                enableJsonString ? JMJson::toJsonString : Object::toString);
    }

    /**
     * Instantiates a new File output.
     *
     * @param filePath         the file path
     * @param toStringFunction
     */
    public JMFileSubscriber(String filePath,
            Function<Object, String> toStringFunction) {
        this.fileAppender = new JMFileAppender(filePath);
        setDataConsumer(o -> JMStream.buildStream(o).map(toStringFunction)
                .forEach(this.fileAppender::appendLine));
    }

    /**
     * Gets file path.
     *
     * @return the file path
     */
    public Path getFilePath() {
        return this.fileAppender.getFilePath();
    }

    @Override
    public void close() {
        this.fileAppender.close();
    }
}
