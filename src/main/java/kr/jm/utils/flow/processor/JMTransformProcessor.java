package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.TransformerInterface;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.concurrent.Flow;
import java.util.function.BiConsumer;

/**
 * The type Jm transform processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMTransformProcessor<T, R> implements
        JMTransformProcessorInterface<T, R> {
    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JMTransformProcessor.class);
    private JMSubscriber<T> inputSubscriber;
    private JMSubmissionPublisher<R> outputPublisher;

    /**
     * Instantiates a new Jm transform processor.
     *
     * @param transformerInterface the transformer interface
     */
    public JMTransformProcessor(
            TransformerInterface<T, R> transformerInterface) {
        this(getSingleInputPublisherBiConsumer(transformerInterface));
    }

    /**
     * Gets single input publisher bi consumer.
     *
     * @param <I>                  the type parameter
     * @param <O>                  the type parameter
     * @param transformerInterface the transformer interface
     * @return the single input publisher bi consumer
     */
    protected static <I, O> BiConsumer<I, JMSubmissionPublisher<? super O>>
    getSingleInputPublisherBiConsumer(
            TransformerInterface<I, O> transformerInterface) {
        return (i, s) -> s.submit(transformerInterface.transform(i));
    }

    /**
     * Instantiates a new Jm transform processor.
     *
     * @param outputPublisherBiConsumer the output publisher bi consumer
     */
    public JMTransformProcessor(
            BiConsumer<T, JMSubmissionPublisher<? super R>>
                    outputPublisherBiConsumer) {
        this.outputPublisher = new JMSubmissionPublisher<>();
        this.inputSubscriber = JMSubscriberBuilder
                .build(t -> outputPublisherBiConsumer
                        .accept(t, outputPublisher));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        JMLog.info(log, "onSubscribe", subscription);
        inputSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T item) {
        JMLog.debug(log, "onNext", item);
        inputSubscriber.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        inputSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        JMLog.info(log, "onComplete");
        inputSubscriber.onComplete();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {
        JMLog.info(log, "subscribeWith", subscriber);
        this.outputPublisher.subscribe(subscriber);
    }
}
