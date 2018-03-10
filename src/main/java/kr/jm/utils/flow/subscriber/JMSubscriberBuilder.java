package kr.jm.utils.flow.subscriber;

import kr.jm.utils.helper.JMJson;

import java.util.function.Consumer;
import java.util.function.Function;

public class JMSubscriberBuilder {

    public static <I> JMSubscriber<I> getSOPLSubscriber() {
        return getSOPLSubscriber(Function.identity());
    }

    public static <I> JMSubscriber<I> getJsonStringSOPLSubscriber() {
        return getJsonStringSOPLSubscriber(Function.identity());
    }

    public static <I> JMSubscriber<I> getSOPLSubscriber(
            Function<I, ?> transformFunction) {
        return build(
                o -> System.out.println(transformFunction.apply(o)));
    }

    public static <I> JMSubscriber<I> getJsonStringSOPLSubscriber(
            Function<I, ?> transformFunction) {
        return getSOPLSubscriber(
                o -> JMJson.toJsonString(transformFunction.apply(o)));
    }

    public static <I> JMSubscriber<I> build(Consumer<I> itemConsumer) {
        return new JMSubscriber<>(itemConsumer);
    }
}
