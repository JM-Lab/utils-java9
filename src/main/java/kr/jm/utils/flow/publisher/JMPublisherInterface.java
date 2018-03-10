package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMOptional;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

public interface JMPublisherInterface<T> extends Flow.Publisher<T> {

    default <R extends Flow.Subscriber<T>> R subscribeAndReturn(
            R returnSubscriber) {
        subscribe(returnSubscriber);
        return returnSubscriber;
    }

    default JMPublisherInterface<T> subscribeWith(
            Flow.Subscriber<T>... subscribers) {
        JMOptional.getOptional(subscribers).map(Arrays::stream)
                .ifPresent(stream -> stream.forEach(this::subscribe));
        return this;
    }

    default JMPublisherInterface<T> consumeWith(Consumer<T>... itemConsumers) {
        JMOptional.getOptional(itemConsumers).map(Arrays::stream)
                .ifPresent(stream -> stream.map(JMSubscriberBuilder::build)
                        .forEach(this::subscribe));
        return this;
    }
}
