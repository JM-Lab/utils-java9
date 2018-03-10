package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.function.Supplier;

import static kr.jm.utils.helper.JMThread.newSingleThreadPool;
import static kr.jm.utils.helper.JMThread.shutdownNowAndWaitToBeTerminated;


/**
 * The type Waiting submission publisher demon.
 *
 * @param <T> the type parameter
 */
public class WaitingSubmissionPublisherDemon<T> extends
        WaitingSubmissionPublisher<T> {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(WaitingSubmissionPublisherDemon.class);

    private ExecutorService executorService;
    private Supplier<T> dataSupplier;

    /**
     * Instantiates a new Waiting submission publisher demon.
     *
     * @param dataSupplier the data supplier
     */
    public WaitingSubmissionPublisherDemon(Supplier<T> dataSupplier) {
        this(null, dataSupplier);
    }

    /**
     * Instantiates a new Waiting submission publisher demon.
     *
     * @param queueSizeLimit the queue size limit
     * @param dataSupplier   the data supplier
     */
    public WaitingSubmissionPublisherDemon(int queueSizeLimit, Supplier<T>
            dataSupplier) {
        this(null, queueSizeLimit, dataSupplier);
    }

    /**
     * Instantiates a new Waiting submission publisher demon.
     *
     * @param executorService the executor service
     * @param dataSupplier    the data supplier
     */
    public WaitingSubmissionPublisherDemon(ExecutorService executorService,
            Supplier<T> dataSupplier) {
        this(executorService, Flow.defaultBufferSize(), dataSupplier);
    }

    /**
     * Instantiates a new Waiting submission publisher demon.
     *
     * @param executorService the executor service
     * @param queueSizeLimit  the queue size limit
     * @param dataSupplier    the data supplier
     */
    public WaitingSubmissionPublisherDemon(ExecutorService executorService,
            int queueSizeLimit, Supplier<T> dataSupplier) {
        super(executorService, queueSizeLimit);
        this.executorService = newSingleThreadPool();
        this.dataSupplier = dataSupplier;
    }

    /**
     * Start waiting submission publisher demon.
     *
     * @return the waiting submission publisher demon
     */
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
