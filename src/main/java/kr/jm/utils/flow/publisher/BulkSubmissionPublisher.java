package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMThread;

import java.util.*;

/**
 * The type Bulk submission publisher.
 *
 * @param <T> the type parameter
 */
public class BulkSubmissionPublisher<T> extends JMListSubmissionPublisher<T> {

    /**
     * The constant DEFAULT_BULK_SIZE.
     */
    public static final int DEFAULT_BULK_SIZE = 100;
    /**
     * The constant DEFAULT_FLUSH_INTERVAL_SECONDS.
     */
    public static final int DEFAULT_FLUSH_INTERVAL_SECONDS = 1;
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
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }


    /**
     * Instantiates a new Bulk submission publisher.
     *
     * @param bulkSize             the bulk size
     * @param flushIntervalSeconds the flush interval seconds
     */
    public BulkSubmissionPublisher(
            int bulkSize, int flushIntervalSeconds) {
        this.bulkSize = bulkSize;
        this.flushIntervalMillis = flushIntervalSeconds * 1000;
        this.dataList = new ArrayList<>();
        this.lastDataTimestamp = Long.MAX_VALUE;
        JMThread.runWithScheduleAtFixedRate(0, flushIntervalMillis,
                this::checkIntervalAndFlush);
    }

    private void checkIntervalAndFlush() {
        if (this.lastDataTimestamp <
                System.currentTimeMillis() - this.flushIntervalMillis) {
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
        return submit(Optional.ofNullable(dataArray).map(Arrays::asList)
                .orElseGet(Collections::emptyList));
    }

    @Override
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
            return dataList.size();
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
                super.submit(this.dataList);
                this.dataList = new ArrayList<>();
            }
        }
    }

}
