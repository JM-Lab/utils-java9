package kr.jm.utils.flow;

import kr.jm.utils.flow.publisher.WaitingSubmissionPublisherDemon;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WaitingSubmissionPublisherDemonTest {
    private WaitingSubmissionPublisherDemon<String>
            submissionWithWaitingPublisherDemon;

    @Before
    public void setUp() {
        Queue<String> stringQueue = new LinkedBlockingQueue<>(
                JMResources.readLines(("webAccessLogSample.txt")));
        this.submissionWithWaitingPublisherDemon =
                new WaitingSubmissionPublisherDemon<>(10,
                        stringQueue::poll);
    }

    @After
    public void tearDown() {
        this.submissionWithWaitingPublisherDemon.close();
    }

    @Test
    public void start() {
        AtomicInteger atomicInteger = new AtomicInteger();
        submissionWithWaitingPublisherDemon.start();
        submissionWithWaitingPublisherDemon
                .subscribe(JMSubscriberBuilder.build(s ->
                        System.out.println((atomicInteger.incrementAndGet() +
                                " line - " + s))));
        JMThread.sleep(1000);
    }
}