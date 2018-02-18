package kr.jm.utils.flow;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class SingleTransformWithThreadPoolProcessor<T, R> extends
        SubmissionPublisher<R> implements Flow.Processor<T, R> {
    private static final Logger log =
            org.slf4j.LoggerFactory
                    .getLogger(SingleTransformWithThreadPoolProcessor.class);
    private SingleSubscriber<T> singleSubscriber;

    public SingleTransformWithThreadPoolProcessor(
            TransformerInterface<T, R> transformerInterface) {
        this(Flow.defaultBufferSize(), transformerInterface);
    }

    public SingleTransformWithThreadPoolProcessor(int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        this(null, maxBufferCapacity, transformerInterface);
    }

    public SingleTransformWithThreadPoolProcessor(Executor executor,
            int maxBufferCapacity,
            TransformerInterface<T, R> transformerInterface) {
        super(executor == null ? JMThread.getCommonPool() : executor,
                maxBufferCapacity);
        this.singleSubscriber =
                SingleSubscriber
                        .build(t -> submit(transformerInterface.transform(t)));
    }

    @Override
    public int submit(R item) {
        JMLog.debug(log, "submit", item);
        return super.submit(item);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        singleSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T item) {
        singleSubscriber.onNext(item);
    }

    public void insert(T item) {singleSubscriber.insert(item);}

    @Override
    public void onError(Throwable throwable) {
        singleSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {singleSubscriber.onComplete();}
}
