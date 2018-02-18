package kr.jm.utils.flow;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.function.Supplier;

import static kr.jm.utils.helper.JMThread.newSingleThreadPool;
import static kr.jm.utils.helper.JMThread.shutdownNowAndWaitToBeTerminated;


public class SubmissionWithWaitingPublisherDemon<T> extends
        SubmissionWithWaitingPublisher<T> {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(SubmissionWithWaitingPublisherDemon.class);

    private ExecutorService executorService;
    private Supplier<T> dataSupplier;

    public SubmissionWithWaitingPublisherDemon(Supplier<T> dataSupplier) {
        this(null, dataSupplier);
    }

    public SubmissionWithWaitingPublisherDemon(int sizeLimit, Supplier<T>
            dataSupplier) {
        this(null, sizeLimit, dataSupplier);
    }

    public SubmissionWithWaitingPublisherDemon(ExecutorService executorService,
            Supplier<T> dataSupplier) {
        this(executorService, Flow.defaultBufferSize(), dataSupplier);
    }

    public SubmissionWithWaitingPublisherDemon(ExecutorService executorService,
            int sizeLimit, Supplier<T> dataSupplier) {
        super(executorService, sizeLimit);
        this.executorService = newSingleThreadPool();
        this.dataSupplier = dataSupplier;
    }

    public SubmissionWithWaitingPublisherDemon<T> start() {
        this.executorService = JMThread.startWithSingleExecutorService(
                "SubmissionWithWaitingPublisherDemon", this::run);
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
