package kr.jm.utils.flow.processor;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.publisher.WaitingSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleSubscriberProcessorTest {

    private JMConcurrentTransformProcessor<String, List<String>>
            singleTransformWithThreadPoolProcessor;
    private JMTransformProcessor<String, List<String>>
            singleTransformProcessor;

    @Before
    public void setUp() {
        this.singleTransformProcessor =
                new JMTransformProcessor<>(JMWordSplitter::splitAsList);
        this.singleTransformWithThreadPoolProcessor =
                new JMConcurrentTransformProcessor<>(3,
                        JMWordSplitter::splitAsList);
    }

    @After
    public void tearDown() {
        this.singleTransformWithThreadPoolProcessor.close();
    }

    @Test
    public void testSubmissionProcessor() {
        AtomicInteger atomicInteger = new AtomicInteger();
        this.singleTransformProcessor
                .subscribe(JMSubscriberBuilder.build(wordList ->
                        System.out.println(
                                atomicInteger.incrementAndGet() + " " +
                                        wordList)));
        WaitingSubmissionPublisher<String> waitingSubmissionPublisher =
                new WaitingSubmissionPublisher<>();
        waitingSubmissionPublisher
                .subscribe(this.singleTransformProcessor);
        JMResources.readLines("webAccessLogSample.txt")
                .forEach(waitingSubmissionPublisher::submit);
        JMThread.sleep(1000);
        Assert.assertEquals(1024, atomicInteger.longValue());

        atomicInteger.set(0);
        this.singleTransformWithThreadPoolProcessor
                .subscribe(JMSubscriberBuilder.build(wordList ->
                        System.out.println(
                                atomicInteger.incrementAndGet() + " " +
                                        wordList)));
        waitingSubmissionPublisher =
                new WaitingSubmissionPublisher<>();
        waitingSubmissionPublisher
                .subscribe(this.singleTransformWithThreadPoolProcessor);
        JMResources.readLines("webAccessLogSample.txt")
                .forEach(waitingSubmissionPublisher::submit);
        JMThread.sleep(1000);
        Assert.assertEquals(1024, atomicInteger.longValue());
    }
}