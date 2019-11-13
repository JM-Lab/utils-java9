package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.publisher.JMPublisherInterface;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The interface Jm processor interface.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface JMProcessorInterface<T, R> extends
        Flow.Processor<T, R>, JMPublisherInterface<R> {
    /**
     * Subscribe and return processor s.
     *
     * @param <O>                      the type parameter
     * @param <S>                      the type parameter
     * @param returnTransformProcessor the return transform processor
     * @return the s
     */
    default <O, S extends JMProcessorInterface<R, O>> S subscribeAndReturnProcessor(
            S returnTransformProcessor) {
        subscribe(returnTransformProcessor);
        return returnTransformProcessor;
    }

    /**
     * Subscribe and return processor jm processor interface.
     *
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm processor interface
     */
    default <O> JMProcessorInterface<R, O> subscribeAndReturnProcessor(
            Function<R, O> transformerFunction) {
        return subscribeAndReturnProcessor(
                JMProcessorBuilder.build(transformerFunction));
    }

    /**
     * Subscribe and return processor with thread pool jm processor interface.
     *
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm processor interface
     */
    default <O> JMProcessorInterface<R, O> subscribeAndReturnProcessorWithThreadPool(
            Function<R, O> transformerFunction) {
        return subscribeAndReturnProcessor(
                JMProcessorBuilder.build(transformerFunction));
    }

    @Override
    default JMProcessorInterface<T, R> subscribeWith(
            Flow.Subscriber<R>... subscribers) {
        JMPublisherInterface.super.subscribeWith(subscribers);
        return this;
    }

    @Override
    default JMProcessorInterface<T, R> consumeWith(
            Consumer<R>... consumers) {
        JMPublisherInterface.super.consumeWith(consumers);
        return this;
    }
}
