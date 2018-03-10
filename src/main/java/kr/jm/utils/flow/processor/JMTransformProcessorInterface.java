package kr.jm.utils.flow.processor;

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
     * @param <O>                            the type parameter
     * @param <S>                            the type parameter
     * @param returnSingleTransformProcessor the return single transform processor
     * @return the s
     */
    default <O, S extends JMTransformProcessorInterface<R, O>> S subscribeAndReturn(
            S returnSingleTransformProcessor) {
        subscribe(returnSingleTransformProcessor);
        return returnSingleTransformProcessor;
    }
}
