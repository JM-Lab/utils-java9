package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;

import java.util.List;


/**
 * The type Bulk waiting submission publisher.
 *
 * @param <T> the type parameter
 */
public class BulkWaitingSubmissionPublisher<T> extends
        WaitingSubmissionPublisher<List<T>> {

    private BulkSubmissionPublisher<T> bulkSubmissionPublisher;

    /**
     * Instantiates a new Bulk waiting submission publisher.
     */
    public BulkWaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     */
    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher) {
        this(bulkSubmissionPublisher, getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param waitingMillis the waiting millis
     */
    public BulkWaitingSubmissionPublisher(long waitingMillis) {
        this(new BulkSubmissionPublisher<>(), waitingMillis);
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     * @param waitingMillis           the waiting millis
     */
    public BulkWaitingSubmissionPublisher(
            BulkSubmissionPublisher<T> bulkSubmissionPublisher,
            long waitingMillis) {
        this(bulkSubmissionPublisher, waitingMillis,
                getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     * @param waitingMillis           the waiting millis
     * @param queueSizeLimit          the queue size limit
     */
    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, long waitingMillis, int queueSizeLimit) {
        super(waitingMillis, queueSizeLimit);
        this.bulkSubmissionPublisher = bulkSubmissionPublisher
                .subscribeWith(JMSubscriberBuilder.build(super::submit));
    }

    /**
     * Submit int.
     *
     * @param dataArray the data array
     * @return the int
     */
    public int submit(T[] dataArray) {
        return this.bulkSubmissionPublisher.submit(dataArray);
    }

    @Override
    public int submit(List<T> itemList) {
        return this.bulkSubmissionPublisher.submit(itemList);
    }

    /**
     * Submit single.
     *
     * @param item the item
     */
    public void submitSingle(T item) {
        this.bulkSubmissionPublisher.submitSingle(item);
    }

    /**
     * Gets bulk submission publisher.
     *
     * @return the bulk submission publisher
     */
    public BulkSubmissionPublisher<T> getBulkSubmissionPublisher() {
        return this.bulkSubmissionPublisher;
    }

    /**
     * Flush.
     */
    public void flush() {this.bulkSubmissionPublisher.flush();}

    @Override
    public void close() {
        JMLog.info(log, "close");
        this.bulkSubmissionPublisher.close();
        super.close();
    }
}