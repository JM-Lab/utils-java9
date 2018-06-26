package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.flow.publisher.SubmissionPublisherImplementsJM;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The type Jm concurrent transform processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMConcurrentTransformProcessor<T, R> extends
        JMTransformProcessor<T, R> implements AutoCloseable {

    private SubmissionPublisher<R> submissionPublisher;

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param transformerFunction the transformer function
     */
    public JMConcurrentTransformProcessor(
            Function<T, R> transformerFunction) {
        this(Flow.defaultBufferSize(), transformerFunction);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param maxBufferCapacity   the max buffer capacity
     * @param transformerFunction the transformer function
     */
    public JMConcurrentTransformProcessor(int maxBufferCapacity,
            Function<T, R> transformerFunction) {
        this(null, maxBufferCapacity, transformerFunction);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param executor            the executor
     * @param maxBufferCapacity   the max buffer capacity
     * @param transformerFunction the transformer function
     */
    public JMConcurrentTransformProcessor(Executor executor,
            int maxBufferCapacity,
            Function<T, R> transformerFunction) {
        this(executor, maxBufferCapacity,
                getSingleInputPublisherBiConsumer(transformerFunction));
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param singlePublisherBiConsumer the single publisher bi consumer
     */
    public JMConcurrentTransformProcessor(
            BiConsumer<T, JMSubmissionPublisher<? super R>> singlePublisherBiConsumer) {
        this(Flow.defaultBufferSize(), singlePublisherBiConsumer);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param maxBufferCapacity         the max buffer capacity
     * @param singlePublisherBiConsumer the single publisher bi consumer
     */
    public JMConcurrentTransformProcessor(int maxBufferCapacity,
            BiConsumer<T, JMSubmissionPublisher<? super R>> singlePublisherBiConsumer) {
        this(null, maxBufferCapacity, singlePublisherBiConsumer);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param executor                  the executor
     * @param maxBufferCapacity         the max buffer capacity
     * @param singlePublisherBiConsumer the single publisher bi consumer
     */
    public JMConcurrentTransformProcessor(Executor executor,
            int maxBufferCapacity,
            BiConsumer<T, JMSubmissionPublisher<? super R>> singlePublisherBiConsumer) {
        super(singlePublisherBiConsumer);
        this.submissionPublisher = new SubmissionPublisherImplementsJM<>(
                executor == null ? JMThread.getCommonPool() : executor,
                maxBufferCapacity);
        super.subscribe(
                JMSubscriberBuilder.build(submissionPublisher::submit));
    }

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {
        JMLog.info(log, "subscribe", subscriber);
        submissionPublisher.subscribe(subscriber);
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        submissionPublisher.close();
    }
}
