package kr.jm.utils.flow.subscriber;

import kr.jm.utils.JMFileAppender;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMStream;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * The type Jm file subscriber.
 *
 * @param <I> the type parameter
 */
public class JMFileSubscriber<I> extends JMSubscriber<I> implements
        AutoCloseable {

    private JMFileAppender fileAppender;

    /**
     * Instantiates a new Jm file subscriber.
     *
     * @param filePath the file path
     */
    public JMFileSubscriber(String filePath) {
        this(filePath, false);
    }

    /**
     * Instantiates a new Jm file subscriber.
     *
     * @param filePath         the file path
     * @param enableJsonString the enable json string
     */
    public JMFileSubscriber(String filePath, boolean enableJsonString) {
        this(filePath,
                enableJsonString ? JMJson::toJsonString : Object::toString);
    }

    /**
     * Instantiates a new Jm file subscriber.
     *
     * @param filePath         the file path
     * @param toStringFunction the to string custom
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
