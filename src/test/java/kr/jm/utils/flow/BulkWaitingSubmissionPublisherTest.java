package kr.jm.utils.flow;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.publisher.BulkWaitingSubmissionPublisher;
import kr.jm.utils.flow.publisher.StringBulkSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMLambda;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BulkWaitingSubmissionPublisherTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private BulkSubmissionPublisher<String> bulkSubmissionPublisher;
    private BulkWaitingSubmissionPublisher<String>
            stringBulkWaitingSubmissionPublisher;

    @Before
    public void setUp() {
        this.bulkSubmissionPublisher =
                new StringBulkSubmissionPublisher(10);
        this.stringBulkWaitingSubmissionPublisher =
                new BulkWaitingSubmissionPublisher<>(
                        this.bulkSubmissionPublisher);
    }

    @Test
    public void submit() {
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        bulkSubmissionPublisher
                .subscribe(JMSubscriberBuilder.getSOPLSubscriber(list ->
                        atomicInteger.incrementAndGet() + " " +
                                count.addAndGet(list.size()) + " list - " +
                                list));
        bulkSubmissionPublisher.submit(
                JMArrays.toArray(
                        JMResources.readLines("webAccessLogSample.txt")));
        System.out.println(atomicInteger);
        JMThread.sleep(2000);
        System.out.println(atomicInteger);
        System.out.println(count);
        Assert.assertEquals(103, atomicInteger.longValue());
        Assert.assertEquals(1024, count.longValue());
    }

    @Test
    public void submitSingle() {
        AtomicInteger atomicInteger = new AtomicInteger();
        AtomicInteger count = new AtomicInteger(-1);
        bulkSubmissionPublisher
                .subscribe(JMSubscriberBuilder.getSOPLSubscriber(list ->
                        atomicInteger.incrementAndGet() + " " +
                                count.addAndGet(list.size()) + " list - " +
                                list));

        Map<Integer, List<String>> listMap = JMLambda.groupBy(
                JMResources.readLines("webAccessLogSample.txt"),
                t -> count.incrementAndGet() / 10);
        count.set(0);
        listMap.values().forEach(bulkSubmissionPublisher::submit);
        listMap.get(0).stream().limit(6)
                .forEach(stringBulkWaitingSubmissionPublisher::submitSingle);
        JMThread.sleep(1000);
        System.out.println(atomicInteger);
        System.out.println(count);
        Assert.assertEquals(103, atomicInteger.longValue());
        Assert.assertEquals(1030, count.longValue());
    }
}