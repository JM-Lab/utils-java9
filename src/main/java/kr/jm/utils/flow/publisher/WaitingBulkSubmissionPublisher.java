package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

/**
 * The type Waiting bulk submission publisher.
 *
 * @param <T> the type parameter
 */
public class WaitingBulkSubmissionPublisher<T> extends
        BulkSubmissionPublisher<T> {

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
        waitingSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submitSingle));
    }

}
