package kr.jm.utils.flow;

@FunctionalInterface
public interface TransformerInterface<T, R> {
    R transform(T input);
}
