package kr.jm.utils.flow;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleSubscriberProcessorTest {

    private SingleTransformWithThreadPoolProcessor<String, List<String>>
            singleTransformWithThreadPoolProcessor;

    @Before
    public void setUp() {
        this.singleTransformWithThreadPoolProcessor =
                new SingleTransformWithThreadPoolProcessor<>(
                        JMThread.getCommonPool(), 2,
                        JMWordSplitter::splitAsList);
    }

    @After
    public void tearDown() {
        this.singleTransformWithThreadPoolProcessor.close();
    }

    @Test
    public void testSubmissionProcessor() {
        AtomicInteger atomicInteger = new AtomicInteger();
        this.singleTransformWithThreadPoolProcessor
                .subscribe(SingleSubscriber.build(wordList ->
                        System.out.println(
                                atomicInteger.incrementAndGet() + " " +
                                        wordList)));
        SubmissionWithWaitingPublisher<String> submissionWithWaitingPublisher =
                new SubmissionWithWaitingPublisher<>();
        submissionWithWaitingPublisher
                .subscribe(this.singleTransformWithThreadPoolProcessor);
        JMResources.readLines("webAccessLogSample.txt")
                .forEach(submissionWithWaitingPublisher::submit);
        JMThread.sleep(1000);
        Assert.assertEquals(1024, atomicInteger.longValue());
    }
}