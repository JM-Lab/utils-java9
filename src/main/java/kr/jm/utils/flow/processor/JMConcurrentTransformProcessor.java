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

public class JMConcurrentTransformProcessor<T, R> extends
        JMTransformProcessor<T, R> implements AutoCloseable {
    private static final Logger log =
            org.slf4j.LoggerFactory
                    .getLogger(JMConcurrentTransformProcessor.class);
    private SubmissionPublisher<R> submissionPublisher;

    public JMConcurrentTransformProcessor(
            TransformerInterface<T, R> transformerInterface) {
        this(Flow.defaultBufferSize(), transformerInterface);
    }

    public JMConcurrentTransformProcessor(int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        this(null, maxBufferCapacity, transformerInterface);
    }

    public JMConcurrentTransformProcessor(Executor executor,
            int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        this(executor, maxBufferCapacity,
                getSingleInputPublisherBiConsumer(transformerInterface));
    }

    public JMConcurrentTransformProcessor(
            BiConsumer<T, JMSubmissionPublisher<? super R>> singlePublisherBiConsumer) {
        this(Flow.defaultBufferSize(), singlePublisherBiConsumer);
    }

    public JMConcurrentTransformProcessor(int maxBufferCapacity,
            BiConsumer<T, JMSubmissionPublisher<? super R>> singlePublisherBiConsumer) {
        this(null, maxBufferCapacity, singlePublisherBiConsumer);
    }

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
