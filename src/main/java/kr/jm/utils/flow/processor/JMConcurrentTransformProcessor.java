package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.TransformerInterface;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.BiConsumer;

/**
 * The type Jm concurrent transform processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMConcurrentTransformProcessor<T, R> extends
        JMTransformProcessor<T, R> implements AutoCloseable {
    private static final Logger log =
            org.slf4j.LoggerFactory
                    .getLogger(JMConcurrentTransformProcessor.class);
    private SubmissionPublisher<R> submissionPublisher;

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param transformerInterface the transformer interface
     */
    public JMConcurrentTransformProcessor(
            TransformerInterface<T, R> transformerInterface) {
        this(Flow.defaultBufferSize(), transformerInterface);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param maxBufferCapacity    the max buffer capacity
     * @param transformerInterface the transformer interface
     */
    public JMConcurrentTransformProcessor(int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        this(null, maxBufferCapacity, transformerInterface);
    }

    /**
     * Instantiates a new Jm concurrent transform processor.
     *
     * @param executor             the executor
     * @param maxBufferCapacity    the max buffer capacity
     * @param transformerInterface the transformer interface
     */
    public JMConcurrentTransformProcessor(Executor executor,
            int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        this(executor, maxBufferCapacity,
                getSingleInputPublisherBiConsumer(transformerInterface));
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
        this.submissionPublisher = new SubmissionPublisher<>(
                executor == null ? JMThread.getCommonPool() : executor,
                maxBufferCapacity);
        super.subscribe(
                JMSubscriberBuilder.build(submissionPublisher::submit));
    }

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {
        JMLog.info(log, "subscribeWith", subscriber);
        submissionPublisher.subscribe(subscriber);
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        submissionPublisher.close();
    }
}
