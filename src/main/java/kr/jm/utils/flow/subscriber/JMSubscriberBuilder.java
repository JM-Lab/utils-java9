package kr.jm.utils.flow.subscriber;

import kr.jm.utils.helper.JMJson;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The type Jm subscriber builder.
 */
public class JMSubscriberBuilder {

    /**
     * Gets sopl subscriber.
     *
     * @param <I> the type parameter
     * @return the sopl subscriber
     */
    public static <I> JMSubscriber<I> getSOPLSubscriber() {
        return getSOPLSubscriber(Function.identity());
    }

    /**
     * Gets json string sopl subscriber.
     *
     * @param <I> the type parameter
     * @return the json string sopl subscriber
     */
    public static <I> JMSubscriber<I> getJsonStringSOPLSubscriber() {
        return getJsonStringSOPLSubscriber(Function.identity());
    }

    /**
     * Gets sopl subscriber.
     *
     * @param <I>               the type parameter
     * @param transformFunction the transform function
     * @return the sopl subscriber
     */
    public static <I> JMSubscriber<I> getSOPLSubscriber(
            Function<I, ?> transformFunction) {
        return build(o -> System.out.println(transformFunction.apply(o)));
    }

    /**
     * Gets json string sopl subscriber.
     *
     * @param <I>               the type parameter
     * @param transformFunction the transform function
     * @return the json string sopl subscriber
     */
    public static <I> JMSubscriber<I> getJsonStringSOPLSubscriber(
            Function<I, ?> transformFunction) {
        return getSOPLSubscriber(
                o -> JMJson.toJsonString(transformFunction.apply(o)));
    }

    public static <I> JMFileSubscriber<I> getFileSubscriber(String filePath) {
        return new JMFileSubscriber<>(filePath);
    }

    /**
     * Build file to json string output subscriber.
     *
     * @param filePath the file path
     * @return the output subscriber
     */
    public static <I> JMFileSubscriber<I> buildJsonStringFileSubscriber(
            String filePath) {
        return new JMFileSubscriber<>(filePath, true);
    }

    public static <I> JMFileSubscriber<I> buildJsonStringFileSubscriber(
            String filePath, Function<Object, String> toStringFunction) {
        return new JMFileSubscriber<>(filePath, toStringFunction);
    }

    /**
     * Build jm subscriber.
     *
     * @param <I>          the type parameter
     * @param itemConsumer the item consumer
     * @return the jm subscriber
     */
    public static <I> JMSubscriber<I> build(Consumer<I> itemConsumer) {
        return new JMSubscriber<>(itemConsumer);
    }
}
