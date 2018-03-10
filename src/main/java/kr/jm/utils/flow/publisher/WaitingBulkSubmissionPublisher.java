package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

public class WaitingBulkSubmissionPublisher<T> extends
        BulkSubmissionPublisher<T> {

    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher) {
        this(waitingSubmissionPublisher, DEFAULT_BULK_SIZE);
    }

    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher,
            int bulkSize) {
        this(waitingSubmissionPublisher, bulkSize,
                DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    public WaitingBulkSubmissionPublisher(
            WaitingSubmissionPublisher<T> waitingSubmissionPublisher,
            int bulkSize, int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
        waitingSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(this::submitSingle));
    }

}
