package kr.jm.utils.flow.processor;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriber;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * The type Jm transform processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMTransformProcessor<T, R> implements
        JMTransformProcessorInterface<T, R> {
    /**
     * The Log.
     */
    protected final Logger log =
            org.slf4j.LoggerFactory.getLogger(getClass());
    private JMSubscriber<T> inputSubscriber;
    private JMSubmissionPublisher<R> outputPublisher;

    /**
     * Instantiates a new Jm transform processor.
     *
     * @param transformFunction the transform function
     */
    public JMTransformProcessor(Function<T, R> transformFunction) {
        this.outputPublisher = new JMSubmissionPublisher<>();
        this.inputSubscriber = JMSubscriberBuilder.build(t -> Optional
                .ofNullable(verifyTransformFunction(transformFunction, t))
                .ifPresent(outputPublisher::submit));
    }

    private <I, O> O verifyTransformFunction(Function<I, O> transformFunction,
            I i) {
        try {
            return transformFunction.apply(i);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "verifyTransformFunction", i);
        }
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
        JMLog.info(log, "subscribe", subscriber);
        this.outputPublisher.subscribe(subscriber);
    }
}
