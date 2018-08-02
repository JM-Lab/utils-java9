package kr.jm.utils.flow.publisher;

import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMThread;

import java.util.concurrent.Flow;
import java.util.function.Consumer;


/**
 * The type Waiting submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingSubmissionPublisher<T> extends
        SubmissionPublisherImplementsJM<T> {

    private long waitingMillis;
    private int maxQueue;

    /**
     * Instantiates a new Waiting submission publisher.
     */
    public WaitingSubmissionPublisher() {
        this(JMThread.DEFAULT_WAITING_MILLIS);
    }

    /**
     * Instantiates a new Waiting submission publisher.
     *
     * @param waitingMillis the waiting millis
     */
    public WaitingSubmissionPublisher(long waitingMillis) {
        this(waitingMillis, getDefaultQueueSizeLimit());
    }

    /**
     * Gets default queue size limit.
     *
     * @return the default queue size limit
     */
    public static int getDefaultQueueSizeLimit() {
        return OS.getAvailableProcessors() * 8;
    }

    /**
     * Instantiates a new Waiting submission publisher.
     *
     * @param waitingMillis the waiting millis
     * @param maxQueue      the max queue
     */
    public WaitingSubmissionPublisher(long waitingMillis, int maxQueue) {
        super(JMThread.newMaxQueueThreadPool(1, waitingMillis,
                maxQueue), maxQueue);
        this.waitingMillis = waitingMillis;
        this.maxQueue = maxQueue;
    }

    /**
     * Gets waiting millis.
     *
     * @return the waiting millis
     */
    public long getWaitingMillis() {
        return waitingMillis;
    }

    /**
     * Gets max queue.
     *
     * @return the max queue
     */
    public int getMaxQueue() {
        return maxQueue;
    }

    @Override
    public WaitingSubmissionPublisher<T> subscribeWith(
            Flow.Subscriber<T>... subscribers) {
        super.subscribeWith(subscribers);
        return this;
    }

    @Override
    public WaitingSubmissionPublisher<T> consumeWith(Consumer<T>... consumers) {
        super.consumeWith(consumers);
        return this;
    }
}