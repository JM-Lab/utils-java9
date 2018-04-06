package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.TransformerInterface;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.BiConsumer;

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
     * @param <O>             the type parameter
     * @param <I>             the type parameter
     * @param <R>             the type parameter
     * @param eachTransformer the each transformer
     * @return the jm transform processor interface
     */
    public static <O, I extends Collection<O>, R> JMTransformProcessorInterface<I, R> buildCollectionEach(
            TransformerInterface<O, R> eachTransformer) {
        return buildBi(
                (collection, singleSubmissionPublisher) -> collection.stream()
                        .map(eachTransformer::transform)
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
     * @param <O>             the type parameter
     * @param <I>             the type parameter
     * @param <R>             the type parameter
     * @param eachTransformer the each transformer
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            TransformerInterface<O, R> eachTransformer) {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize(),
                eachTransformer);
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>               the type parameter
     * @param <I>               the type parameter
     * @param <R>               the type parameter
     * @param maxBufferCapacity the max buffer capacity
     * @param eachTransformer   the each transformer
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            int maxBufferCapacity, TransformerInterface<O, R> eachTransformer) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity,
                eachTransformer);
    }

    /**
     * Build collection each with thread pool jm concurrent transform processor.
     *
     * @param <O>               the type parameter
     * @param <I>               the type parameter
     * @param <R>               the type parameter
     * @param executor          the executor
     * @param maxBufferCapacity the max buffer capacity
     * @param eachTransformer   the each transformer
     * @return the jm concurrent transform processor
     */
    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            Executor executor, int maxBufferCapacity,
            TransformerInterface<O, R> eachTransformer) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                (collection, singleSubmissionPublisher) -> collection.stream
                        ().map(eachTransformer::transform)
                        .forEach(singleSubmissionPublisher::submit));
    }

    /**
     * Build jm transform processor interface.
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
     * @param <I>         the type parameter
     * @param <O>         the type parameter
     * @param transformer the transformer
     * @return the jm transform processor
     */
    public static <I, O> JMTransformProcessor<I, O> build(
            TransformerInterface<I, O> transformer) {
        return new JMTransformProcessor<>(transformer);
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
     * @param <I>         the type parameter
     * @param <O>         the type parameter
     * @param transformer the transformer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            TransformerInterface<I, O> transformer) {
        return buildWithThreadPool(Flow.defaultBufferSize(), transformer);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>               the type parameter
     * @param <O>               the type parameter
     * @param maxBufferCapacity the max buffer capacity
     * @param transformer       the transformer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            int maxBufferCapacity,
            TransformerInterface<I, O> transformer) {
        return buildWithThreadPool(null, maxBufferCapacity, transformer);
    }

    /**
     * Build with thread pool jm concurrent transform processor.
     *
     * @param <I>               the type parameter
     * @param <O>               the type parameter
     * @param executor          the executor
     * @param maxBufferCapacity the max buffer capacity
     * @param transformer       the transformer
     * @return the jm concurrent transform processor
     */
    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Executor executor, int maxBufferCapacity,
            TransformerInterface<I, O> transformer) {
        return new JMConcurrentTransformProcessor<>(executor,
                maxBufferCapacity, transformer);
    }

    /**
     * Build combine jm transform processor interface.
     *
     * @param <T>          the type parameter
     * @param <M>          the type parameter
     * @param <R>          the type parameter
     * @param transformer1 the transformer 1
     * @param transformer2 the transformer 2
     * @return the jm transform processor interface
     */
    public static <T, M, R> JMTransformProcessorInterface<T, R> buildCombine(
            TransformerInterface<T, M> transformer1,
            TransformerInterface<M, R> transformer2) {
        return build(t -> transformer2.transform(transformer1.transform(t)));
    }
}
