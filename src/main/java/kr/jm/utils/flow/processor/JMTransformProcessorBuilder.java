package kr.jm.utils.flow.processor;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * The type Jm transform processor builder.
 */
public class JMTransformProcessorBuilder {

    public static <I, R> JMConcurrentTransformProcessor<Collection<I>, R> buildCollectionEachWithThreadPool(
            Function<Collection<I>, R> collectionTransformFunction) {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize(),
                collectionTransformFunction);
    }

    public static <I, R> JMConcurrentTransformProcessor<Collection<I>, R> buildCollectionEachWithThreadPool(
            int maxBufferCapacity,
            Function<Collection<I>, R> collectionTransformFunction) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity,
                collectionTransformFunction);
    }

    public static <I, R> JMConcurrentTransformProcessor<Collection<I>, R>
    buildCollectionEachWithThreadPool(Executor executor, int maxBufferCapacity,
            Function<Collection<I>, R> collectionTransformFunction) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                collectionTransformFunction);
    }

    /**
     * Build jm transform processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm transform processor
     */
    public static <I, O> JMTransformProcessor<I, O> build(
            Function<I, O> transformerFunction) {
        return new JMTransformProcessor<>(transformerFunction);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param transformerFunction the transformer function
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Function<I, O> transformerFunction) {
        return buildWithThreadPool(Flow.defaultBufferSize(),
                transformerFunction);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param maxBufferCapacity   the max buffer capacity
     * @param transformerFunction the transformer function
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            int maxBufferCapacity,
            Function<I, O> transformerFunction) {
        return buildWithThreadPool(null, maxBufferCapacity,
                transformerFunction);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>                 the type parameter
     * @param <O>                 the type parameter
     * @param executor            the executor
     * @param maxBufferCapacity   the max buffer capacity
     * @param transformerFunction the transformer function
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Executor executor, int maxBufferCapacity,
            Function<I, O> transformerFunction) {
        return new JMConcurrentTransformProcessor<>(executor,
                maxBufferCapacity, transformerFunction);
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
    public static <T, M, R> JMTransformProcessorInterface<T, R> buildCombine(
            Function<T, M> transformerFunction1,
            Function<M, R> transformerFunction2) {
        return build(
                t -> transformerFunction2.apply(transformerFunction1.apply(t)));
    }
}
