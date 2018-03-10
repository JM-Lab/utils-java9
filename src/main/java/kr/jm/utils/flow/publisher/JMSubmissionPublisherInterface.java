package kr.jm.utils.flow.publisher;

/**
 * The interface Jm submission publisher interface.
 *
 * @param <T> the type parameter
 */
public interface JMSubmissionPublisherInterface<T> extends
        JMPublisherInterface<T> {
    /**
     * Submit int.
     *
     * @param item the item
     * @return the int
     */
    int submit(T item);
}
