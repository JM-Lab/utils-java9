package kr.jm.utils.flow.publisher;

import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMThread;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The type Waiting submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingSubmissionPublisher<T> extends
        SubmissionPublisherImplementsJM<T> {

    private int queueSizeLimit;

    /**
     * Instantiates a new Waiting submission publisher.
     */
    public WaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new Waiting submission publisher.
     *
     * @param queueSizeLimit the queue size limit
     */
    public WaitingSubmissionPublisher(int queueSizeLimit) {
        this(null, queueSizeLimit);
    }

    /**
     * Instantiates a new Waiting submission publisher.
     *
     * @param executor the executor
     */
    public WaitingSubmissionPublisher(Executor executor) {
        this(executor, getDefaultQueueSizeLimit());
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
     * @param executor       the executor
     * @param queueSizeLimit the queue size limit
     */
    public WaitingSubmissionPublisher(Executor executor,
            int queueSizeLimit) {
        super(Objects.isNull(executor) ? JMThread
                .newSingleThreadPool() : executor, queueSizeLimit);
        this.queueSizeLimit = queueSizeLimit;
    }

    private boolean checkSuspendCondition(int lag, int waitCount) {
        if (isClosed() || lag < queueSizeLimit)
            return false;
        logWaiting(lag, waitCount);
        return true;
    }

    private boolean checkSuspendCondition(AtomicInteger waitCount) {
        return checkSuspendCondition(estimateMaximumLag(),
                waitCount.incrementAndGet());
    }

    private void logWaiting(int lag, int waitCount) {
        if (100 % waitCount == 0 || waitCount % 600 == 0)
            log.warn("Wait Occur !!! - queueSizeLimit = {}, lag = {}",
                    queueSizeLimit, lag);
    }

    @Override
    public int submit(T data) {
        if (isClosed() || Objects.isNull(data))
            return 0;
        AtomicInteger waitCount = new AtomicInteger();
        JMThread.suspend(100, () -> checkSuspendCondition(waitCount));
        try {
            return super.submit(data);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "submit", () -> 0, data);
        }
    }

    /**
     * Gets size limit.
     *
     * @return the size limit
     */
    public int getSizeLimit() {
        return queueSizeLimit;
    }

}