package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.publisher.JMPublisherInterface;

import java.util.concurrent.Flow;

public interface JMTransformProcessorInterface<T, R> extends
        Flow.Processor<T, R>, JMPublisherInterface<R> {
    default <O, S extends JMTransformProcessorInterface<R, O>> S subscribeAndReturn(
            S returnSingleTransformProcessor) {
        subscribe(returnSingleTransformProcessor);
        return returnSingleTransformProcessor;
    }
}
