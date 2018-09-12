package kr.jm.utils.flow.publisher;

import kr.jm.utils.StdInLineConsumer;

/**
 * The type Std in line publisher.
 */
public class StdInLinePublisher extends JMSubmissionPublisher<String> {

    private StdInLineConsumer stdInLineConsumer;

    /**
     * Instantiates a new Std in line publisher.
     */
    public StdInLinePublisher() {
        this.stdInLineConsumer = new StdInLineConsumer(this::submit);
    }

    /**
     * Consume std in std in line publisher.
     *
     * @return the std in line publisher
     */
    public StdInLinePublisher consumeStdIn() {
        stdInLineConsumer.consumeStdIn();
        return this;
    }

    @Override
    public void close() {
        stdInLineConsumer.close();
        super.close();
    }
}
