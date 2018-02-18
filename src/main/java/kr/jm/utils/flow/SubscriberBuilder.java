package kr.jm.utils.flow;

import kr.jm.utils.helper.JMJson;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;

public class SubscriberBuilder {

    public static <T> Flow.Subscriber<T> getSOPLSubscriber() {
        return getSOPLSubscriber(Function.identity());
    }

    public static <T> Flow.Subscriber<T> getJsonStringSOPLSubscriber() {
        return getJsonStringSOPLSubscriber(Function.identity());
    }

    public static <T> Flow.Subscriber<T> getSOPLSubscriber(
            Function<T, ?> transformFunction) {
        return build(
                o -> System.out.println(transformFunction.apply(o)));
    }

    public static <T> Flow.Subscriber<T> getJsonStringSOPLSubscriber(
            Function<T, ?> transformFunction) {
        return getSOPLSubscriber(
                o -> JMJson.toJsonString(transformFunction.apply(o)));
    }

    public static <T> Flow.Subscriber<T> build(Consumer<T> itemConsumer) {
        return SingleSubscriber.build(itemConsumer);
    }
}
