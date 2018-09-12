package kr.jm.utils.flow.processor;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.publisher.ResourceSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import kr.jm.utils.helper.JMString;
import kr.jm.utils.helper.JMThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JMProcessorBuilderTest {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    private List<String> lineList;

    @Before
    public void setUp() {
        this.lineList = JMResources.readLines("webAccessLogSample.txt");
    }

    @Test
    public void test() {
        ResourceSubmissionPublisher listSubmissionPublisher = new
                ResourceSubmissionPublisher();
        JMProcessorInterface<List<String>, String>
                listStringJMTransformProcessor =
                listSubmissionPublisher.subscribeAndReturnSubcriber(
                        JMProcessorBuilder.build(lineList -> JMString
                                .joiningWith(lineList.stream(),
                                        JMString.LINE_SEPARATOR)));
        listStringJMTransformProcessor.subscribeAndReturnSubcriber(
                JMSubscriberBuilder.getSOPLSubscriber());
        JMProcessorInterface<List<String>, List<List<String>>>
                listStringJMTransformProcessor2 =
                listSubmissionPublisher.subscribeAndReturnSubcriber(
                        JMProcessorBuilder.build(lineList ->
                                lineList.stream()
                                        .map(JMWordSplitter::splitAsList)
                                        .collect(Collectors.toList())));
        List<Integer> wordCountList = new ArrayList<>();
        listStringJMTransformProcessor2.subscribeWith(JMSubscriberBuilder
                .build(lists -> lists.stream().map(List::size).forEach(
                        wordCountList::add)))
                .subscribe(JMSubscriberBuilder.getSOPLSubscriber());
        listSubmissionPublisher.submit(lineList);
        JMThread.sleep(1000);

        Assert.assertEquals(lineList.size(), wordCountList.size());
        Assert.assertEquals(34267,
                wordCountList.stream().mapToInt(Integer::intValue).sum());

    }

}