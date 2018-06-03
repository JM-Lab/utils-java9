package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.publisher.JMSubmissionPublisher;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The type Jm transform processor builder.
 */
public class JMTransformProcessorBuilder {
    /**
     * Build collection each jm transform processor interface.
     *
     * @param <O> the type parameter
     * @param <I> the type parameter
     * @return the jm transform processor interface
     */
    public static <O, I extends Collection<O>> JMTransformProcessorInterface<I, O> buildCollectionEach() {
        return buildBi((collection, singleSubmissionPublisher) -> collection
                .forEach(singleSubmissionPublisher::submit));
    }

    /**
     * Build collection each jm transform processor interface.
     *
     * @param <O>                     the type parameter
     * @param <I>                     the type parameter
     * @param <R>                     the type parameter
     * @param eachTransformerFunction the each transformer function
     * @return the jm transform processor interface
     */
    public static <O, I extends Collection<O>, R> JMTransformProcessorInterface<I, R> buildCollectionEach(
            Function<O, R> eachTransformerFunction) {
        return buildBi(
                (collection, singleSubmissionPublisher) -> collection.stream()
                        .map(eachTransformerFunction::apply)
                        .forEach(singleSubmissionPublisher::submit));
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O> the type parameter
     * @param <I> the type parameter
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool() {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize());
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>               the type parameter
     * @param <I>               the type parameter
     * @param maxBufferCapacity the max buffer capacity
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool(
            int maxBufferCapacity) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity);
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>               the type parameter
     * @param <I>               the type parameter
     * @param executor          the executor
     * @param maxBufferCapacity the max buffer capacity
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool(
            Executor executor, int maxBufferCapacity) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                (collection, singleSubmissionPublisher) -> collection
                        .forEach(singleSubmissionPublisher::submit));
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>                     the type parameter
     * @param <I>                     the type parameter
     * @param <R>                     the type parameter
     * @param eachTransformerFunction the each transformer function
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            Function<O, R> eachTransformerFunction) {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize(),
                eachTransformerFunction);
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>                     the type parameter
     * @param <I>                     the type parameter
     * @param <R>                     the type parameter
     * @param maxBufferCapacity       the max buffer capacity
     * @param eachTransformerFunction the each transformer function
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            int maxBufferCapacity, Function<O, R> eachTransformerFunction) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity,
                eachTransformerFunction);
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>                     the type parameter
     * @param <I>                     the type parameter
     * @param <R>                     the type parameter
     * @param executor                the executor
     * @param maxBufferCapacity       the max buffer capacity
     * @param eachTransformerFunction the each transformer function
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            Executor executor, int maxBufferCapacity,
            Function<O, R> eachTransformerFunction) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                (collection, singleSubmissionPublisher) -> collection.stream
                        ().map(eachTransformerFunction::apply)
                        .forEach(singleSubmissionPublisher::submit));
    }

    /**
     * Build bi jm transform processor interface.
     *
     * @param <I>                       the type parameter
     * @param <O>                       the type parameter
     * @param singlePublisherBiConsumer the single publisher bi consumer
     * @return the jm transform processor interface
     */
    public static <I, O> JMTransformProcessorInterface<I, O> buildBi(
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return new JMTransformProcessor<>(singlePublisherBiConsumer);
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
     * @param <I>                       the type parameter
     * @param <O>                       the type parameter
     * @param singlePublisherBiConsumer the single publisher bi consumer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return buildWithThreadPool(Flow.defaultBufferSize(),
                singlePublisherBiConsumer);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>                       the type parameter
     * @param <O>                       the type parameter
     * @param maxBufferCapacity         the max buffer capacity
     * @param singlePublisherBiConsumer the single publisher bi consumer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            int maxBufferCapacity,
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return buildWithThreadPool(null, maxBufferCapacity,
                singlePublisherBiConsumer);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>                       the type parameter
     * @param <O>                       the type parameter
     * @param executor                  the executor
     * @param maxBufferCapacity         the max buffer capacity
     * @param singlePublisherBiConsumer the single publisher bi consumer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Executor executor, int maxBufferCapacity,
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return new JMConcurrentTransformProcessor<>(executor,
                maxBufferCapacity, singlePublisherBiConsumer);
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
