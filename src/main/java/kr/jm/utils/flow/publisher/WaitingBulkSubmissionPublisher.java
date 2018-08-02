package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;

/**
 * The type Waiting bulk submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingBulkSubmissionPublisher<T> extends
        BulkSubmissionPublisher<T> {

    private WaitingSubmissionPublisher<T> waitingSubmissionPublisher;

    /**
     * Instantiates a new Waiting bulk submission publisher.
     *
     * @param waitingSubmissionPublisher the waiting submission publisher
     */
    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher) {
        this(waitingSubmissionPublisher, DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new Waiting bulk submission publisher.
     *
     * @param waitingSubmissionPublisher the waiting submission publisher
     * @param bulkSize                   the bulk size
     */
    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher,
            int bulkSize) {
        this(waitingSubmissionPublisher, bulkSize,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new Waiting bulk submission publisher.
     *
     * @param waitingSubmissionPublisher the waiting submission publisher
     * @param bulkSize                   the bulk size
     * @param flushIntervalSeconds       the flush interval seconds
     */
    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher,
            int bulkSize, int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
        this.waitingSubmissionPublisher = waitingSubmissionPublisher
                .subscribeWith(JMSubscriberBuilder.build(this::submitSingle));
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        this.waitingSubmissionPublisher.close();
        super.close();
    }
}
