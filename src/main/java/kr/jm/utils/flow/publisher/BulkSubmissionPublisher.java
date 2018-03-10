package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMThread;

import java.util.*;

public class BulkSubmissionPublisher<T> extends JMListSubmissionPublisher<T> {

    public static final int DEFAULT_BULK_SIZE = 100;
    public static final int DEFAULT_FLUSH_INTERVAL_SECONDS = 1;
    protected int bulkSize;
    protected long flushIntervalMillis;
    protected List<T> dataList;
    protected long lastDataTimestamp;

    public BulkSubmissionPublisher() {
        this(DEFAULT_BULK_SIZE);
    }

    public BulkSubmissionPublisher(int bulkSize) {
        this(bulkSize, DEFAULT_FLUSH_INTERVAL_SECONDS);
    }


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
