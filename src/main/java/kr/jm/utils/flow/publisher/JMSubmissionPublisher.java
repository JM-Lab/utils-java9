package kr.jm.utils.flow.publisher;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;

/**
 * The type Jm submission publisher.
 *
 * @param <T> the type parameter
 */
public class JMSubmissionPublisher<T> extends
        SubmissionPublisher<T> implements JMPublisherInterface<T> {
    private int publishers;
    private int maxBufferCapacity;
    private long waitingMillis;
    /**
     * The Log.
     */
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public JMSubmissionPublisher(int publishers, int maxBufferCapacity,
            long waitingMillis) {
        super(publishers > 0 ? JMThread.newThreadPool(publishers) : ForkJoinPool
                .commonPool(), maxBufferCapacity);
        this.publishers = publishers;
        this.maxBufferCapacity = maxBufferCapacity;
        this.waitingMillis = waitingMillis;
    }

    public JMSubmissionPublisher(int publishers, int maxBufferCapacity) {
        this(publishers, maxBufferCapacity, JMThread.DEFAULT_WAITING_MILLIS);
    }

    public JMSubmissionPublisher(int publishers) {
        this(publishers, Flow.defaultBufferSize());
    }

    public JMSubmissionPublisher() {
        this(0);
    }

    @Override
    public int submit(T item) {
        JMLog.debug(log, "submit", item);
        try {
            return waiting(super.submit(item));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "submit", () -> 0,
                            item);
        }
    }

    private int waiting(int rag) throws InterruptedException {
        if (rag >= this.maxBufferCapacity) {
            JMLog.warn(log, "waiting", waitingMillis);
            Thread.sleep(waitingMillis);
        }
        return rag;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        JMLog.debug(log, "subscribe", subscriber);
        super.subscribe(subscriber);
    }

    @Override
    public String toString() {
        return "JMSubmissionPublisher{" + "publishers=" + publishers +
                ", maxBufferCapacity=" + maxBufferCapacity +
                ", waitingMillis=" + waitingMillis + '}';
    }

}
