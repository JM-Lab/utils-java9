package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * The type Jm submission publisher.
 *
 * @param <T> the type parameter
 */
public class JMSubmissionPublisher<T> implements
        JMSubmissionPublisherInterface<T> {
    /**
     * The Log.
     */
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    private SingleSubscription singleSubscription;
    private List<Flow.Subscriber<? super T>> subscriberList;

    /**
     * Instantiates a new Jm submission publisher.
     */
    public JMSubmissionPublisher() {
        this(() -> null);
    }

    /**
     * Instantiates a new Jm submission publisher.
     *
     * @param inputSupplier the input supplier
     */
    public JMSubmissionPublisher(Supplier<T> inputSupplier) {
        this.singleSubscription = new SingleSubscription(inputSupplier);
        this.subscriberList = new ArrayList<>();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        JMLog.info(log, "subscribeWith", subscriber);
        subscriber.onSubscribe(singleSubscription);
        this.subscriberList.add(subscriber);
    }

    @Override
    public int submit(T item) {
        JMLog.debug(log, "submit", item);
        if (Objects.isNull(item))
            return 0;
        singleSubscription.next(item);
        return 1;
    }

    private class SingleSubscription implements Flow.Subscription {
        private Supplier<T> inputSupplier;
        private AtomicBoolean isCanceled;

        /**
         * Instantiates a new Single subscription.
         *
         * @param inputSupplier the input supplier
         */
        public SingleSubscription(Supplier<T> inputSupplier) {
            this.inputSupplier = inputSupplier;
            this.isCanceled = new AtomicBoolean(false);
        }

        @Override
        public void request(long n) {
            if (isNotCanceledOrNonNullSubscriber())
                for (int i = 0; i < n; i++)
                    Optional.ofNullable(inputSupplier.get())
                            .ifPresent(this::next);
        }

        private boolean isNotCanceledOrNonNullSubscriber() {
            return !isCanceled() || nonNullSubscriber();
        }

        private boolean isCanceled() {
            boolean isCanceled = this.isCanceled.get();
            if (isCanceled)
                log.warn("isCanceled = {} !!!", this.isCanceled);
            return isCanceled;
        }

        @Override
        public void cancel() {
            JMLog.info(log, "cancel");
            isCanceled.set(true);
        }

        private void next(T data) {
            if (isNotCanceledOrNonNullSubscriber() && nonNullData(data))
                for (Flow.Subscriber<? super T> subscriber : subscriberList)
                    subscriber.onNext(data);
        }

        private boolean nonNullData(T data) {
            return nonNullWithWarnLog("data", data);
        }

        private boolean nonNullSubscriber() {
            return nonNullWithWarnLog("subscriberList", subscriberList);
        }

        private boolean nonNullWithWarnLog(String targetName, Object
                target) {
            if (Objects.nonNull(target))
                return true;
            log.warn("{} = null", targetName);
            return false;
        }
    }


}
