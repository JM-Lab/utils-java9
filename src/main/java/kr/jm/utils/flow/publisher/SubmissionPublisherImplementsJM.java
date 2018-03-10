package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.BiConsumer;

public class SubmissionPublisherImplementsJM<T> extends
        SubmissionPublisher<T> implements JMSubmissionPublisherInterface<T> {
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public SubmissionPublisherImplementsJM(Executor executor,
            int maxBufferCapacity,
            BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> handler) {
        super(executor, maxBufferCapacity, handler);
    }

    public SubmissionPublisherImplementsJM(Executor executor,
            int maxBufferCapacity) {
        super(executor, maxBufferCapacity);
    }

    public SubmissionPublisherImplementsJM() {
        super();
    }

    @Override
    public int submit(T data) {
        JMLog.debug(log, "submit", data);
        return super.submit(data);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        JMLog.debug(log, "subscribeWith", subscriber);
        super.subscribe(subscriber);
    }

}