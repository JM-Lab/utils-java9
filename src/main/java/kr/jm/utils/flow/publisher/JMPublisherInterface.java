package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMOptional;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * The interface Jm publisher interface.
 *
 * @param <T> the type parameter
 */
@SuppressWarnings("ALL")
public interface JMPublisherInterface<T> extends Flow.Publisher<T> {

    /**
     * Subscribe and return subcriber r.
     *
     * @param <R>              the type parameter
     * @param returnSubscriber the return subscriber
     * @return the r
     */
    default <R extends Flow.Subscriber<T>> R subscribeAndReturnSubcriber(
            R returnSubscriber) {
        subscribe(returnSubscriber);
        return returnSubscriber;
    }

    /**
     * Consume and return subscriber flow . subscriber.
     *
     * @param consumer the consumer
     * @return the flow . subscriber
     */
    default Flow.Subscriber<T> consumeAndReturnSubscriber(
            Consumer<T> consumer) {
        return subscribeAndReturnSubcriber(JMSubscriberBuilder.build(consumer));
    }

    /**
     * Subscribe with jm publisher interface.
     *
     * @param subscribers the subscribers
     * @return the jm publisher interface
     */
    default JMPublisherInterface<T> subscribeWith(
            Flow.Subscriber<T>... subscribers) {
        JMOptional.getOptional(subscribers).map(Arrays::stream)
                .ifPresent(stream -> stream.forEach(this::subscribe));
        return this;
    }

    /**
     * Consume with jm publisher interface.
     *
     * @param consumers the consumers
     * @return the jm publisher interface
     */
    default JMPublisherInterface<T> consumeWith(Consumer<T>... consumers) {
        JMOptional.getOptional(consumers).map(Arrays::stream)
                .ifPresent(stream -> stream.map(JMSubscriberBuilder::build)
                        .forEach(this::subscribe));
        return this;
    }
}
