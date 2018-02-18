package kr.jm.utils.flow;

import kr.jm.utils.enums.OS;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;


public class SubmissionWithWaitingPublisher<T> extends SubmissionPublisher<T> {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(SubmissionWithWaitingPublisher.class);

    private int sizeLimit;

    public SubmissionWithWaitingPublisher() {
        this(null);
    }

    public SubmissionWithWaitingPublisher(int sizeLimit) {
        this(null, sizeLimit);
    }

    public SubmissionWithWaitingPublisher(Executor executor) {
        this(executor, getDefaultSizeLimit());
    }

    public static int getDefaultSizeLimit() {
        return OS.getAvailableProcessors() * 8;
    }

    public SubmissionWithWaitingPublisher(Executor executor,
            int sizeLimit) {
        super(executor == null ? JMThread.getCommonPool() : executor,
                sizeLimit);
        this.sizeLimit = sizeLimit;
    }

    private boolean checkSuspendCondition(int lag, int waitCount) {
        return lag >= sizeLimit && logWaiting(lag, waitCount);
    }

    private boolean checkSuspendCondition(AtomicInteger waitCount) {
        return checkSuspendCondition(estimateMaximumLag(),
                waitCount.incrementAndGet());
    }

    private boolean logWaiting(int lag, int waitCount) {
        if (waitCount % 600 == 0 || 100 % waitCount == 0)
            log.warn("Wait Occur !!! - sizeLimit = {}, lag = {}",
                    sizeLimit, lag);
        return true;
    }

    @Override
    public int submit(T item) {
        try {
            if (isClosed())
                return 0;
            AtomicInteger waitCount = new AtomicInteger();
            JMThread.suspend(100, () -> checkSuspendCondition(waitCount));
            return super.submit(item);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturn(log, e, "submitWithWaiting",
                            () -> 0, item);
        }
    }

    public int getSizeLimit() {
        return sizeLimit;
    }

}