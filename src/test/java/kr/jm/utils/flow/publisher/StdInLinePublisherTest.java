package kr.jm.utils.flow.publisher;

import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMStream;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class StdInLinePublisherTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private StdInLinePublisher stdInLinePublisher;
    private PrintWriter printWriter;

    @Before
    public void setUp() throws Exception {
        this.stdInLinePublisher = new StdInLinePublisher();
        PipedInputStream pin = new PipedInputStream();
        System.setIn(pin);
        this.printWriter = new PrintWriter(new PipedOutputStream(pin));
    }

    @After
    public void tearDown() {
        this.stdInLinePublisher.close();
        this.printWriter.close();
    }

    @Test
    public void consumeStdIn() {
        List<String> resultLineList = new ArrayList<>();
        this.stdInLinePublisher
                .subscribeWith(JMSubscriberBuilder.getSOPLSubscriber())
                .subscribe(JMSubscriberBuilder.build(resultLineList::add));
        this.stdInLinePublisher.consumeStdIn();

        List<String> stdInLineList =
                JMResources.readLines("webAccessLogSample.txt");
        stdInLineList.forEach(this.printWriter::println);
        this.printWriter.flush();
        JMThread.sleep(1000);
        Assert.assertEquals(stdInLineList.size(), resultLineList.size());
        Assert.assertTrue(JMStream.increaseRange(stdInLineList.size()).allMatch(
                i -> stdInLineList.get(i).equals(resultLineList.get(i))));
    }
}