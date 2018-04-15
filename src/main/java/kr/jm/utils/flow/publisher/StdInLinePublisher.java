package kr.jm.utils.flow.publisher;

import kr.jm.utils.StdInLineConsumer;

public class StdInLinePublisher extends JMSubmissionPublisher<String> implements
        AutoCloseable {

    private StdInLineConsumer stdInLineConsumer;

    public StdInLinePublisher() {
        this.stdInLineConsumer = new StdInLineConsumer(this::submit);
    }

    public StdInLinePublisher consumeStdIn() {
        stdInLineConsumer.consumeStdIn();
        return this;
    }

    @Override
    public void close() {
        stdInLineConsumer.close();
    }
}
