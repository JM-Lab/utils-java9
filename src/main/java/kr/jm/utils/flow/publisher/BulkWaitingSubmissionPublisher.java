package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;

import java.util.List;
import java.util.concurrent.Executor;


public class BulkWaitingSubmissionPublisher<T> extends
        WaitingSubmissionPublisher<List<T>> {

    private BulkSubmissionPublisher<T> bulkSubmissionPublisher;

    public BulkWaitingSubmissionPublisher() {
        this(getDefaultQueueSizeLimit());

    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher) {
        this(bulkSubmissionPublisher, null);
    }

    public BulkWaitingSubmissionPublisher(int queueSizeLimit) {
        this(new BulkSubmissionPublisher<>(), queueSizeLimit);
    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, int queueSizeLimit) {
        this(bulkSubmissionPublisher, null, queueSizeLimit);
    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, Executor executor) {
        this(bulkSubmissionPublisher, executor, getDefaultQueueSizeLimit());
    }

    public BulkWaitingSubmissionPublisher(BulkSubmissionPublisher<T>
            bulkSubmissionPublisher, Executor executor, int queueSizeLimit) {
        super(executor, queueSizeLimit);
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
}