package kr.jm.utils.flow.processor;

import kr.jm.utils.enums.OS;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.function.Function;

/**
 * The type Jm transform processor builder.
 */
public class JMProcessorBuilder {

    /**
     * Build jm transform processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm transform processor
     */
    public static <I, O> JMProcessor<I, O> build(
            Function<I, O> transformerFunction) {
        return new JMProcessor<>(transformerFunction);
    }

    public static <T, M, R> Processor<T, R> combine(
            Processor<T, M> processor1, Processor<M, R> processor2) {
        processor1.subscribe(processor2);
        return new Processor<>() {
            @Override
            public void subscribe(Flow.Subscriber<? super R> subscriber) {
                processor2.subscribe(subscriber);
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                processor1.onSubscribe(subscription);
            }

            @Override
            public void onNext(T item) {processor1.onNext(item);}

            @Override
            public void onError(Throwable throwable) {
                processor1.onError(throwable);
            }

            @Override
            public void onComplete() {processor1.onComplete();}
        };
    }

    public static <I, O> JMConcurrentProcessor<I, O> buildWithThreadPool(
            Function<I, O> transformerFunction) {
        return buildWithThreadPool(OS.getAvailableProcessors(),
                transformerFunction);
    }

    public static <I, O> JMConcurrentProcessor<I, O> buildWithThreadPool(
            int workers, Function<I, O> transformerFunction) {
        return new JMConcurrentProcessor<>(workers, transformerFunction);
    }
}
