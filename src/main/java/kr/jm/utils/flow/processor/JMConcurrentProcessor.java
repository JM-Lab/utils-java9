package kr.jm.utils.flow.processor;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMThread;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * The type Jm concurrent processor.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
public class JMConcurrentProcessor<T, R> extends
        JMProcessor<T, R> {

    private ExecutorService executorService;

    /**
     * Instantiates a new Jm concurrent processor.
     *
     * @param workers             the workers
     * @param transformerFunction the transformer function
     */
    public JMConcurrentProcessor(int workers,
            Function<T, R> transformerFunction) {
        super(transformerFunction);
        this.executorService = JMThread.newThreadPool(workers);
    }

    @Override
    protected void process(T input) {
        this.executorService.submit(() -> super.process(input));
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        JMThread.awaitTermination(this.executorService, 3000);
        super.close();
    }
}
