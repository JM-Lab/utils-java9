package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLog;

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
        this.bulkSubmissionPublisher = bulkSubmissionPublisher
                .subscribeWith(JMSubscriberBuilder.build(super::submit));
    }

    public int submit(T[] dataArray) {
        return this.bulkSubmissionPublisher.submit(dataArray);
    }

    @Override
    public int submit(List<T> itemList) {
        return this.bulkSubmissionPublisher.submit(itemList);
    }

    public void submitSingle(T item) {
        this.bulkSubmissionPublisher.submitSingle(item);
    }

    public BulkSubmissionPublisher<T> getBulkSubmissionPublisher() {
        return this.bulkSubmissionPublisher;
    }

    public void flush() {this.bulkSubmissionPublisher.flush();}

    @Override
    public void close() {
        JMLog.info(log, "close");
        this.bulkSubmissionPublisher.close();
        super.close();
    }
}