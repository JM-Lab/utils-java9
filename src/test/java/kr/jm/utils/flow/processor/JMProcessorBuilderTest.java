package kr.jm.utils.flow.processor;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.publisher.LineSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class JMProcessorBuilderTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    @Test
    public void test() {
        AtomicInteger index = new AtomicInteger();
        LineSubmissionPublisher lineSubmissionPublisher =
                new LineSubmissionPublisher();
        JMProcessor<String, String>
                listStringJMTransformProcessor =
                lineSubmissionPublisher.subscribeAndReturnSubcriber(
                        JMProcessorBuilder.buildWithThreadPool(line -> index
                                .getAndIncrement() + " " + line));
        listStringJMTransformProcessor.subscribeAndReturnSubcriber(
                JMSubscriberBuilder.getSOPLSubscriber());
        JMProcessor<String, List<String>>
                listStringJMTransformProcessor2 =
                listStringJMTransformProcessor.subscribeAndReturnSubcriber(
                        JMProcessorBuilder.build(line -> JMWordSplitter
                                .splitAsList(line)));
        List<Integer> wordCountList = new ArrayList<>();
        listStringJMTransformProcessor2.subscribeWith(JMSubscriberBuilder
                .build(list -> wordCountList.add(list.size())))
                .subscribe(JMSubscriberBuilder.getSOPLSubscriber());
        lineSubmissionPublisher.submitClasspath("webAccessLogSample.txt");
        JMThread.sleep(1000);

        Assert.assertEquals(1024, wordCountList.size());
        Assert.assertEquals(35291,
                wordCountList.stream().mapToInt(Integer::intValue).sum());

    }

}