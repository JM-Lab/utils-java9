package kr.jm.utils.flow.publisher;

public class StringBulkSubmissionPublisher extends
        BulkSubmissionPublisher<String> implements
        StringListSubmissionPublisherInterface {

    public StringBulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    public StringBulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    public StringBulkSubmissionPublisher(int bulkSize,
            int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
    }

}
