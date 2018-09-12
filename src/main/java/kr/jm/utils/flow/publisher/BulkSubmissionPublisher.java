package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.SubmissionPublisher;

/**
 * The type Bulk submission publisher.
 *
 * @param <T> the type parameter
 */
public class BulkSubmissionPublisher<T> implements
        JMPublisherInterface<List<T>>, AutoCloseable {
    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    /**
     * The constant DEFAULT_BULK_SIZE.
     */
    public static final int DEFAULT_BULK_SIZE = 256;
    /**
     * The constant DEFAULT_FLUSH_INTERVAL_Millis.
     */
    public static final long DEFAULT_FLUSH_INTERVAL_Millis = 100;

    private SubmissionPublisher<List<T>> listSubmissionPublisher;
    /**
     * The Bulk size.
     */
    protected int bulkSize;
    /**
     * The Flush interval millis.
     */
    protected long flushIntervalMillis;
    /**
     * The Data list.
     */
    protected List<T> dataList;
    /**
     * The Last data timestamp.
     */
    protected long lastDataTimestamp;

    private ScheduledFuture<?> scheduledFuture;

    /**
     * Instantiates a new Bulk submission publisher.
     */
    public BulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    /**
     * Instantiates a new Bulk submission publisher.
     *
     * @param bulkSize the bulk size
     */
    public BulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_Millis);
    }


    /**
     * Instantiates a new Bulk submission publisher.
     *
     * @param bulkSize            the bulk size
     * @param flushIntervalMillis the flush interval seconds
     */
    public BulkSubmissionPublisher(int bulkSize, long flushIntervalMillis) {
        this(new JMSubmissionPublisher<>(), bulkSize, flushIntervalMillis);
    }

    public BulkSubmissionPublisher(
            SubmissionPublisher<List<T>> listSubmissionPublisher, int bulkSize,
            long flushIntervalMillis) {
        this.listSubmissionPublisher = listSubmissionPublisher;
        this.bulkSize = bulkSize;
        this.flushIntervalMillis = flushIntervalMillis;
        this.dataList = new ArrayList<>();
        this.lastDataTimestamp = Long.MAX_VALUE;
        this.scheduledFuture =
                JMThread.runWithScheduleAtFixedRate(this.flushIntervalMillis,
                        this.flushIntervalMillis, this::checkIntervalAndFlush);
    }

    private void checkIntervalAndFlush() {
        if (this.lastDataTimestamp <
                System.currentTimeMillis() - this.flushIntervalMillis &&
                this.dataList.size() > 0) {
            JMLog.warn(log, "checkIntervalAndFlush", this
                    .lastDataTimestamp, this.flushIntervalMillis);
            flush();
        }
    }

    /**
     * Submit int.
     *
     * @param dataArray the data array
     * @return the int
     */
    public int submit(T[] dataArray) {
        return submitBulk(Optional.ofNullable(dataArray).map(Arrays::asList)
                .orElseGet(Collections::emptyList));
    }

    public int submit(List<T> itemList) {
        return JMOptional.getOptional(itemList).map(this::submitBulk).orElse(0);
    }

    private int submitBulk(List<T> dataList) {
        synchronized (this.dataList) {
            if (this.dataList.size() + dataList.size() < this.bulkSize) {
                this.dataList.addAll(dataList);
                setLastDataTimestamp();
            } else
                for (T data : dataList)
                    submitSingle(data);
            return this.dataList.size();
        }
    }

    /**
     * Submit single int.
     *
     * @param item the item
     * @return the int
     */
    public int submitSingle(T item) {
        if (Objects.isNull(item))
            return 0;
        synchronized (this.dataList) {
            this.dataList.add(item);
            setLastDataTimestamp();
            if (this.dataList.size() >= this.bulkSize)
                flush();
            return 1;
        }
    }

    private void setLastDataTimestamp() {
        this.lastDataTimestamp = System.currentTimeMillis();
    }

    /**
     * Flush.
     */
    public void flush() {
        JMLog.debug(log, "flush", this.dataList.size());
        synchronized (this.dataList) {
            if (this.dataList.size() > 0) {
                this.listSubmissionPublisher.submit(this.dataList);
                this.dataList = new ArrayList<>();
            }
        }
    }

    @Override
    public void close() {
        JMLog.info(log, "close");
        this.scheduledFuture.cancel(false);
        this.listSubmissionPublisher.close();
    }

    @Override
    public String toString() {
        return "BulkSubmissionPublisher(listSubmissionPublisher=" +
                listSubmissionPublisher.toString() + ", bulkSize=" +
                this.bulkSize + ", flushIntervalMillis=" +
                this.flushIntervalMillis + ", dataList=" + this.dataList + ")";
    }


    @Override
    public void subscribe(Flow.Subscriber<? super List<T>> subscriber) {
        this.listSubmissionPublisher.subscribe(subscriber);
    }
}
