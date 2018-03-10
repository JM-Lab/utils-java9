package kr.jm.utils.flow.processor;

import kr.jm.utils.flow.TransformerInterface;
import kr.jm.utils.flow.publisher.JMSubmissionPublisher;
import kr.jm.utils.helper.JMConsumer;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.BiConsumer;

public class JMTransformProcessorBuilder {
    public static <O, I extends Collection<O>> JMTransformProcessorInterface<I, O> buildCollectionEach() {
        return build((collection, singleSubmissionPublisher) -> collection
                .forEach(singleSubmissionPublisher::submit));
    }

    public static <O, I extends Collection<O>, R> JMTransformProcessorInterface<I, R> buildCollectionEach(
            TransformerInterface<O, R> eachTransformer) {
        try {
            return build(
                    (collection, singleSubmissionPublisher) -> collection
                            .stream()
                            .map(eachTransformer::transform)
                            .peek(JMConsumer.getSOPL())
                            .forEach(singleSubmissionPublisher::submit));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return build(
                (collection, singleSubmissionPublisher) -> collection.stream()
                        .map(eachTransformer::transform)
                        .peek(JMConsumer.getSOPL())
                        .forEach(singleSubmissionPublisher::submit));
    }

    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool() {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize());
    }

    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool(
            int maxBufferCapacity) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity);
    }

    public static <O, I extends Collection<O>> JMConcurrentTransformProcessor<I, O> buildCollectionEachWithThreadPool(
            Executor executor, int maxBufferCapacity) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                (collection, singleSubmissionPublisher) -> collection
                        .forEach(singleSubmissionPublisher::submit));
    }

    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            TransformerInterface<O, R> eachTransformer) {
        return buildCollectionEachWithThreadPool(Flow.defaultBufferSize(),
                eachTransformer);
    }

    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            int maxBufferCapacity, TransformerInterface<O, R> eachTransformer) {
        return buildCollectionEachWithThreadPool(null, maxBufferCapacity,
                eachTransformer);
    }

    public static <O, I extends Collection<O>, R>
    JMConcurrentTransformProcessor<I, R> buildCollectionEachWithThreadPool(
            Executor executor, int maxBufferCapacity,
            TransformerInterface<O, R> eachTransformer) {
        return buildWithThreadPool(executor, maxBufferCapacity,
                (collection, singleSubmissionPublisher) -> collection.stream
                        ().map(eachTransformer::transform)
                        .forEach(singleSubmissionPublisher::submit));
    }

    public static <I, O> JMTransformProcessorInterface<I, O> build(
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return new JMTransformProcessor<>(singlePublisherBiConsumer);
    }

    public static <I, O> JMTransformProcessor<I, O> build(
            TransformerInterface<I, O> transformer) {
        return new JMTransformProcessor<>(transformer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return buildWithThreadPool(Flow.defaultBufferSize(),
                singlePublisherBiConsumer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            int maxBufferCapacity,
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return buildWithThreadPool(null, maxBufferCapacity,
                singlePublisherBiConsumer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Executor executor, int maxBufferCapacity,
            BiConsumer<I, JMSubmissionPublisher<? super O>> singlePublisherBiConsumer) {
        return new JMConcurrentTransformProcessor<>(executor,
                maxBufferCapacity, singlePublisherBiConsumer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            TransformerInterface<I, O> transformer) {
        return buildWithThreadPool(Flow.defaultBufferSize(), transformer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            int maxBufferCapacity,
            TransformerInterface<I, O> transformer) {
        return buildWithThreadPool(null, maxBufferCapacity, transformer);
    }

    public static <I, O> JMConcurrentTransformProcessor<I, O> buildWithThreadPool(
            Executor executor, int maxBufferCapacity,
            TransformerInterface<I, O> transformer) {
        return new JMConcurrentTransformProcessor<>(executor,
                maxBufferCapacity, transformer);
    }

    public static <T, M, R> JMTransformProcessorInterface<T, R> buildCombine(
            TransformerInterface<T, M> transformer1,
            TransformerInterface<M, R> transformer2) {
        return build(t -> transformer2.transform(transformer1.transform(t)));
    }
}
