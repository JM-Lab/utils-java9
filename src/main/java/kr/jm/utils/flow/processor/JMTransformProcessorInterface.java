package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.TransformerInterface;
import kr.jm.utils.flow.publisher.JMPublisherInterface;

import java.util.concurrent.Flow;

/**
 * The interface Jm transform processor interface.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public interface JMTransformProcessorInterface<T, R> extends
        Flow.Processor<T, R>, JMPublisherInterface<R> {
    /**
     * Subscribe and return s.
     *
     * @param <O>                      the type parameter
     * @param <S>                      the type parameter
     * @param returnTransformProcessor the return single transform processor
     * @return the s
     */
    default <O, S extends JMTransformProcessorInterface<R, O>> S subscribeAndReturnProcessor(
            S returnTransformProcessor) {
        subscribe(returnTransformProcessor);
        return returnTransformProcessor;
    }

    default <O> JMTransformProcessorInterface<R, O> subscribeAndReturnProcessor(
            TransformerInterface<R, O> transformerInterface) {
        return subscribeAndReturnProcessor(
                JMTransformProcessorBuilder.build(transformerInterface));
    }

    default <O> JMTransformProcessorInterface<R, O> subscribeAndReturnProcessorWithThreadPool(
            TransformerInterface<R, O> transformerInterface) {
        return subscribeAndReturnProcessor(JMTransformProcessorBuilder
                .buildWithThreadPool(transformerInterface));
    }
}
