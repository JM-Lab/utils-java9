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

public class WaitingSubmissionPublisherTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private WaitingSubmissionPublisher<String>
            waitingSubmissionPublisher;

    @Before
    public void setUp() {
        this.waitingSubmissionPublisher =
                new WaitingSubmissionPublisher<>(100);
    }

    @After
    public void tearDown() {
        this.waitingSubmissionPublisher.close();
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
        waitingSubmissionPublisher.subscribe(subscriber);
        JMFiles.getLineStream(path).filter(s -> s.length() % 2 == 0)
                .forEach(waitingSubmissionPublisher::submit);
        waitingSubmissionPublisher.consume(s
                -> System.out.println(atomicInteger.incrementAndGet()
                + "- singleSubscriber - " + s));
        JMFiles.getLineStream(path).filter(s -> s.length() % 2 == 1)
                .forEach(waitingSubmissionPublisher::submit);
        JMThread.sleep(3000);

        Assert.assertEquals(1575, atomicInteger.intValue());

    }

}