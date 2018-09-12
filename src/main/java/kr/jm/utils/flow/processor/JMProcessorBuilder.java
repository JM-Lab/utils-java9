package kr.jm.utils.flow.processor;

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

    /**
     * Build combine jm transform processor interface.
     *
     * @param <T>                  the type parameter
     * @param <M>                  the type parameter
     * @param <R>                  the type parameter
     * @param transformerFunction1 the transformer function 1
     * @param transformerFunction2 the transformer function 2
     * @return the jm transform processor interface
     */
    public static <T, M, R> JMProcessorInterface<T, R> buildCombine(
            Function<T, M> transformerFunction1,
            Function<M, R> transformerFunction2) {
        return build(
                t -> transformerFunction2.apply(transformerFunction1.apply(t)));
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
}
