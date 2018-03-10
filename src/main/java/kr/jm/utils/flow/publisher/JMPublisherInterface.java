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
public interface JMPublisherInterface<T> extends Flow.Publisher<T> {

    /**
     * Subscribe and return r.
     *
     * @param <R>              the type parameter
     * @param returnSubscriber the return subscriber
     * @return the r
     */
    default <R extends Flow.Subscriber<T>> R subscribeAndReturn(
            R returnSubscriber) {
        subscribe(returnSubscriber);
        return returnSubscriber;
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
     * @param itemConsumers the item consumers
     * @return the jm publisher interface
     */
    default JMPublisherInterface<T> consumeWith(Consumer<T>... itemConsumers) {
        JMOptional.getOptional(itemConsumers).map(Arrays::stream)
                .ifPresent(stream -> stream.map(JMSubscriberBuilder::build)
                        .forEach(this::subscribe));
        return this;
    }
}
