package kr.jm.utils.flow.publisher;

import kr.jm.utils.helper.JMFiles;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public class JMSubmissionPublisherTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private JMSubmissionPublisher<String> submissionPublisher;

    @Before
    public void setUp() {
        this.submissionPublisher = new JMSubmissionPublisher<>(3);
    }

    @After
    public void tearDown() {
        this.submissionPublisher.close();
    }

    @Test
    public void testSubmitFilePaths() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Flow.Subscriber<? super String> subscriber =
                new Flow.Subscriber<>() {
                    Flow.Subscription subscription;

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(String item) {
                        System.out.println(atomicInteger.incrementAndGet() +
                                " - onNext - " + item);
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete !!!");
                    }
                };

        String path = JMResources.getURI("webAccessLogSample.txt").getPath();
        System.out.println(path);
        submissionPublisher.subscribe(subscriber);
        submissionPublisher.consume(s
                -> System.out.println(atomicInteger.incrementAndGet()
                + "- singleSubscriber - " + s));
        JMFiles.getLineStream(path).forEach(submissionPublisher::submit);
        JMThread.sleep(1000);

        Assert.assertEquals(2048, atomicInteger.intValue());

    }

}