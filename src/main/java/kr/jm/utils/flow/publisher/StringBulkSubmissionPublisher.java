package kr.jm.utils.flow.publisher;

/**
 * The type String bulk submission publisher.
 */
public class StringBulkSubmissionPublisher extends
        BulkSubmissionPublisher<String> implements
        StringListSubmissionPublisherInterface {

    /**
     * Instantiates a new String bulk submission publisher.
     */
    public StringBulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new String bulk submission publisher.
     *
     * @param bulkSize the bulk size
     */
    public StringBulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }

    /**
     * Instantiates a new String bulk submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public StringBulkSubmissionPublisher(int bulkSize,
            int flushIntervalSeconds) {
        super(bulkSize, flushIntervalSeconds);
    }

}
