package kr.jm.utils.flow.processor;

import kr.jm.utils.enums.OS;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.function.Function;

/**
 * The type Jm processor builder.
 */
public class JMProcessorBuilder {

    /**
     * Build jm processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer custom
     * @return the jm processor
     */
    public static <I, O> JMProcessor<I, O> build(
            Function<I, O> transformerFunction) {
        return new JMProcessor<>(transformerFunction);
    }

    /**
     * Combine processor.
     *
     * @param <T>        the type parameter
     * @param <M>        the type parameter
     * @param <R>        the type parameter
     * @param processor1 the processor 1
     * @param processor2 the processor 2
     * @return the processor
     */
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

    /**
     * Build with thread pool jm concurrent processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer custom
     * @return the jm concurrent processor
     */
    public static <I, O> JMConcurrentProcessor<I, O> buildWithThreadPool(
            Function<I, O> transformerFunction) {
        return buildWithThreadPool(OS.getAvailableProcessors(),
                transformerFunction);
    }

    /**
     * Build with thread pool jm concurrent processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param workers             the workers
     * @param transformerFunction the transformer custom
     * @return the jm concurrent processor
     */
    public static <I, O> JMConcurrentProcessor<I, O> buildWithThreadPool(
            int workers, Function<I, O> transformerFunction) {
        return new JMConcurrentProcessor<>(workers, transformerFunction);
    }
}
