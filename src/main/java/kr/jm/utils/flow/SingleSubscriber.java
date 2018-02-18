package kr.jm.utils.flow;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

public class SingleSubscriber<T> implements Flow.Subscriber<T> {

    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(SingleSubscriber.class);
    private Flow.Subscription subscription;
    private Consumer<T> itemConsumer;

    public static <R> SingleSubscriber<R> build(Consumer<R> itemConsumer) {
        return new SingleSubscriber<>(itemConsumer);
    }

    private SingleSubscriber(Consumer<T> itemConsumer) {
        this.itemConsumer = itemConsumer;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        JMLog.info(log, "onSubscribe", subscription);
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        JMLog.debug(log, "onNext", item);
        insert(item);
        subscription.request(1);
    }

    public void insert(T item) {
        if (item == null)
            JMLog.warn(log, "insert", item);
        else {
            JMLog.debug(log, "insert", item);
            itemConsumer.accept(item);
        }
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
