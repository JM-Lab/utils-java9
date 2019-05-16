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
 * The type Jm processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMProcessor<T, R> implements
        JMProcessorInterface<T, R>, AutoCloseable {
    /**
     * The Log.
     */
    protected final Logger log =
            org.slf4j.LoggerFactory.getLogger(getClass());
    private Function<T, R> transformFunction;
    private JMSubmissionPublisher<R> outputPublisher;
    private JMSubscriber<T> inputSubscriber;

    /**
     * Instantiates a new Jm processor.
     *
     * @param transformFunction the transform custom
     */
    public JMProcessor(Function<T, R> transformFunction) {
        this.transformFunction = transformFunction;
        this.outputPublisher = new JMSubmissionPublisher<>();
        this.inputSubscriber = JMSubscriberBuilder.build(this::process);
    }

    /**
     * Process.
     *
     * @param input the input
     */
    protected void process(T input) {
        try {
            Optional.ofNullable(this.transformFunction.apply(input))
                    .ifPresent(this.outputPublisher::submit);
        } catch (Exception e) {
            JMExceptionManager.handleException(log, e, "process", input);
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

    @Override
    public void close() {
        JMLog.info(log, "close");
        this.outputPublisher.close();
    }
}
