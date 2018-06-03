package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.publisher.JMPublisherInterface;

import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * The interface Jm transform processor interface.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface JMTransformProcessorInterface<T, R> extends
        Flow.Processor<T, R>, JMPublisherInterface<R> {
    /**
     * Subscribe and return processor s.
     *
     * @param <O>                      the type parameter
     * @param <S>                      the type parameter
     * @param returnTransformProcessor the return transform processor
     * @return the s
     */
    default <O, S extends JMTransformProcessorInterface<R, O>> S subscribeAndReturnProcessor(
            S returnTransformProcessor) {
        subscribe(returnTransformProcessor);
        return returnTransformProcessor;
    }

    /**
     * Subscribe and return processor jm transform processor interface.
     *
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm transform processor interface
     */
    default <O> JMTransformProcessorInterface<R, O> subscribeAndReturnProcessor(
            Function<R, O> transformerFunction) {
        return subscribeAndReturnProcessor(
                JMTransformProcessorBuilder.build(transformerFunction));
    }

    /**
     * Subscribe and return processor with thread pool jm transform processor interface.
     *
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm transform processor interface
     */
    default <O> JMTransformProcessorInterface<R, O> subscribeAndReturnProcessorWithThreadPool(
            Function<R, O> transformerFunction) {
        return subscribeAndReturnProcessor(JMTransformProcessorBuilder
                .buildWithThreadPool(transformerFunction));
    }
}
