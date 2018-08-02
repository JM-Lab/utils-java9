package kr.jm.utils.flow.publisher;

import kr.jm.utils.enums.OS;
import kr.jm.utils.helper.JMThread;

import java.util.concurrent.Flow;
import java.util.function.Consumer;


public class WaitingSubmissionPublisher<T> extends
        SubmissionPublisherImplementsJM<T> {

    private long waitingMillis;
    private int maxQueue;

    public WaitingSubmissionPublisher() {
        this(JMThread.DEFAULT_WAITING_MILLIS);
    }

    public WaitingSubmissionPublisher(long waitingMillis) {
        this(waitingMillis, getDefaultQueueSizeLimit());
    }

    public static int getDefaultQueueSizeLimit() {
        return OS.getAvailableProcessors() * 8;
    }

    public WaitingSubmissionPublisher(long waitingMillis, int maxQueue) {
        super(JMThread.newMaxQueueThreadPool(1, waitingMillis,
                maxQueue), maxQueue);
        this.waitingMillis = waitingMillis;
        this.maxQueue = maxQueue;
    }

    public long getWaitingMillis() {
        return waitingMillis;
    }

    public int getMaxQueue() {
        return maxQueue;
    }

    @Override
    public WaitingSubmissionPublisher<T> subscribeWith(
            Flow.Subscriber<T>... subscribers) {
        super.subscribeWith(subscribers);
        return this;
    }

    @Override
    public WaitingSubmissionPublisher<T> consumeWith(Consumer<T>... consumers) {
        super.consumeWith(consumers);
        return this;
    }
}