package kr.jm.utils.flow;

/**
 * The interface Transformer interface.
 *
 * @param <T> the type parameter
 * @param <R> the type parameter
 */
@FunctionalInterface
public interface TransformerInterface<T, R> {
    /**
     * Transform r.
     *
     * @param input the input
     * @return the r
     */
    R transform(T input);
}
