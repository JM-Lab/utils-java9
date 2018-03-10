package kr.jm.utils.flow;

import kr.jm.utils.datastructure.JMArrays;
import kr.jm.utils.flow.publisher.BulkSubmissionPublisher;
import kr.jm.utils.flow.publisher.WaitingBulkSubmissionPublisher;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
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

public class BulkSubmissionPublisherTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private BulkSubmissionPublisher<String> bulkSubmissionPublisher;
    private WaitingSubmissionPublisher<String> stringWaitingSubmissionPublisher;

    @Before
    public void setUp() {
        this.stringWaitingSubmissionPublisher =
                new WaitingSubmissionPublisher<>();
        this.bulkSubmissionPublisher =
                new WaitingBulkSubmissionPublisher<>(
                        stringWaitingSubmissionPublisher, 10);
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
                .forEach(stringWaitingSubmissionPublisher::submit);
        JMThread.sleep(1000);
        System.out.println(atomicInteger);
        System.out.println(count);
        Assert.assertEquals(103, atomicInteger.longValue());
        Assert.assertEquals(1030, count.longValue());
    }
}