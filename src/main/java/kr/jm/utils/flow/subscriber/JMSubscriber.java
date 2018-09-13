package kr.jm.utils.flow.subscriber;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * The type Jm subscriber.
 *
 * @param <T> the type parameter
 */
public class JMSubscriber<T> implements Flow.Subscriber<T> {

    /**
     * The Log.
     */
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private Flow.Subscription subscription;
    private Consumer<T> dataConsumer;

    /**
     * Instantiates a new Jm subscriber.
     */
    protected JMSubscriber() {
        this.dataConsumer =
                d -> JMExceptionManager.handleException(log,
                        JMExceptionManager.newRunTimeException(
                                "DataConsumer Wasn't Set !!! - Flush " + d),
                        "JMSubscriber");
    }

    /**
     * Instantiates a new Jm subscriber.
     *
     * @param dataConsumer the data consumer
     */
    public JMSubscriber(Consumer<T> dataConsumer) {
        setDataConsumer(dataConsumer);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        JMLog.info(log, "onSubscribe", subscription);
        requestNext(this.subscription = subscription);
    }

    private void requestNext(Flow.Subscription subscription) {
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        JMLog.debug(log, "onNext", item);
        Optional.ofNullable(item).ifPresent(this.dataConsumer);
        Optional.ofNullable(this.subscription).ifPresent(this::requestNext);
    }

    @Override
    public void onError(Throwable throwable) {
        JMExceptionManager.handleException(log, throwable, "onError");
    }

    @Override
    public void onComplete() {
        JMLog.info(log, "onComplete");
    }

    /**
     * Sets data consumer.
     *
     * @param dataConsumer the data consumer
     */
    public void setDataConsumer(Consumer<T> dataConsumer) {
        this.dataConsumer = dataConsumer;
    }
}
