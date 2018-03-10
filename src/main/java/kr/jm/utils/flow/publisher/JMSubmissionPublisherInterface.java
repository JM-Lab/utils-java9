package kr.jm.utils.flow.publisher;

public interface JMSubmissionPublisherInterface<T> extends
        JMPublisherInterface<T> {
    int submit(T item);
}
