package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.function.Supplier;

import static kr.jm.utils.helper.JMThread.newSingleThreadPool;
import static kr.jm.utils.helper.JMThread.shutdownNowAndWaitToBeTerminated;


public class WaitingSubmissionPublisherDemon<T> extends
        WaitingSubmissionPublisher<T> {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(WaitingSubmissionPublisherDemon.class);

    private ExecutorService executorService;
    private Supplier<T> dataSupplier;

    public WaitingSubmissionPublisherDemon(Supplier<T> dataSupplier) {
        this(null, dataSupplier);
    }

    public WaitingSubmissionPublisherDemon(int queueSizeLimit, Supplier<T>
            dataSupplier) {
        this(null, queueSizeLimit, dataSupplier);
    }

    public WaitingSubmissionPublisherDemon(ExecutorService executorService,
            Supplier<T> dataSupplier) {
        this(executorService, Flow.defaultBufferSize(), dataSupplier);
    }

    public WaitingSubmissionPublisherDemon(ExecutorService executorService,
            int queueSizeLimit, Supplier<T> dataSupplier) {
        super(executorService, queueSizeLimit);
        this.executorService = newSingleThreadPool();
        this.dataSupplier = dataSupplier;
    }

    public WaitingSubmissionPublisherDemon<T> start() {
        this.executorService = JMThread.startWithSingleExecutorService(
                "WaitingSubmissionPublisherDemon", this::run);
        return this;
    }

    private void run() {
        JMLog.info(log, "run");
        while (!executorService.isShutdown())
            submit(JMThread.suspendWhenNull(100, dataSupplier));
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        shutdownNowAndWaitToBeTerminated(executorService);
        super.close();
    }
}
