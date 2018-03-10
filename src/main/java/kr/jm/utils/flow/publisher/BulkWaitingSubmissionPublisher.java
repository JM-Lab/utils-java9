package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

import java.util.List;
import java.util.concurrent.Executor;


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
        this(bulkSubmissionPublisher, null);
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param queueSizeLimit the queue size limit
     */
    public BulkWaitingSubmissionPublisher(int queueSizeLimit) {
        this(new BulkSubmissionPublisher<>(), queueSizeLimit);
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     * @param queueSizeLimit          the queue size limit
     */
    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, int queueSizeLimit) {
        this(bulkSubmissionPublisher, null, queueSizeLimit);
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     * @param executor                the executor
     */
    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, Executor executor) {
        this(bulkSubmissionPublisher, executor, getDefaultQueueSizeLimit());
    }

    /**
     * Instantiates a new Bulk waiting submission publisher.
     *
     * @param bulkSubmissionPublisher the bulk submission publisher
     * @param executor                the executor
     * @param queueSizeLimit          the queue size limit
     */
    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, Executor executor, int queueSizeLimit) {
        super(executor, queueSizeLimit);
        bulkSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(super::submit));
        this.bulkSubmissionPublisher = bulkSubmissionPublisher;
    }

    /**
     * Submit int.
     *
     * @param dataArray the data array
     * @return the int
     */
    public int submit(T[] dataArray) {
        return bulkSubmissionPublisher.submit(dataArray);
    }

    @Override
    public int submit(List<T> itemList) {
        return bulkSubmissionPublisher.submit(itemList);
    }

    /**
     * Submit single.
     *
     * @param item the item
     */
    public void submitSingle(T item) {
        bulkSubmissionPublisher.submitSingle(item);
    }

    /**
     * Gets bulk submission publisher.
     *
     * @return the bulk submission publisher
     */
    public BulkSubmissionPublisher<T> getBulkSubmissionPublisher() {
        return bulkSubmissionPublisher;
    }
}