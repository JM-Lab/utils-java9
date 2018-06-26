package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

import java.util.List;


public class BulkWaitingSubmissionPublisher<T> extends
        WaitingSubmissionPublisher<List<T>> {

    private BulkSubmissionPublisher<T> bulkSubmissionPublisher;

    public BulkWaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());
    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher) {
        this(bulkSubmissionPublisher, getDefaultQueueSizeLimit());
    }

    public BulkWaitingSubmissionPublisher(long waitingMillis) {
        this(new BulkSubmissionPublisher<>(), waitingMillis);
    }

    public BulkWaitingSubmissionPublisher(
            BulkSubmissionPublisher<T> bulkSubmissionPublisher,
            long waitingMillis) {
        this(bulkSubmissionPublisher, waitingMillis,
                getDefaultQueueSizeLimit());
    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, long waitingMillis, int queueSizeLimit) {
        super(waitingMillis, queueSizeLimit);
        bulkSubmissionPublisher
                .subscribe(JMSubscriberBuilder.build(super::submit));
        this.bulkSubmissionPublisher = bulkSubmissionPublisher;
    }

    public int submit(T[] dataArray) {
        return bulkSubmissionPublisher.submit(dataArray);
    }

    @Override
    public int submit(List<T> itemList) {
        return bulkSubmissionPublisher.submit(itemList);
    }

    public void submitSingle(T item) {
        bulkSubmissionPublisher.submitSingle(item);
    }

    public BulkSubmissionPublisher<T> getBulkSubmissionPublisher() {
        return bulkSubmissionPublisher;
    }

    public void flush() {bulkSubmissionPublisher.flush();}
}