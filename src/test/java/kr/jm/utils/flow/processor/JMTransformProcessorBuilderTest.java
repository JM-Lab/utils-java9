package kr.jm.utils.flow.processor;

import kr.jm.utils.JMWordSplitter;
import kr.jm.utils.flow.publisher.StringListSubmissionPublisher;
import kr.jm.utils.flow.subscriber.JMSubscriberBuilder;
import kr.jm.utils.helper.JMResources;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JMTransformProcessorBuilderTest {

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
        StringListSubmissionPublisher listSubmissionPublisher = new
                StringListSubmissionPublisher();
        JMTransformProcessorInterface<List<String>, String>
                listStringJMTransformProcessor =
                listSubmissionPublisher.subscribeAndReturn(
                        JMTransformProcessorBuilder.buildCollectionEach());
        listStringJMTransformProcessor.subscribeAndReturn(
                JMSubscriberBuilder.getSOPLSubscriber());
        JMTransformProcessorInterface<List<String>, List<String>>
                listStringJMTransformProcessor2 =
                listSubmissionPublisher.subscribeAndReturn(
                        JMTransformProcessorBuilder.buildCollectionEach
                                (line -> Arrays
                                        .asList(JMWordSplitter.split(line))));
        listStringJMTransformProcessor2.subscribeAndReturn(
                JMSubscriberBuilder.getSOPLSubscriber());
        listSubmissionPublisher.submit(lineList);
    }


}