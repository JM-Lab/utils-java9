package kr.jm.utils.flow.subscriber;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class JMSubscriber<T> implements Flow.Subscriber<T> {

    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JMSubscriber.class);
    private Flow.Subscription subscription;
    private Consumer<T> itemConsumer;

    public JMSubscriber(Consumer<T> itemConsumer) {
        this.itemConsumer = itemConsumer;
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
        Optional.ofNullable(item).ifPresent(itemConsumer);
        Optional.ofNullable(this.subscription).ifPresent(this::requestNext);
    }

    @Override
    public void onError(Throwable throwable) {
        JMExceptionManager.logException(log, throwable, "onError");
    }

    @Override
    public void onComplete() {
        JMLog.info(log, "onComplete");
    }
}
