package kr.jm.utils.flow;

public interface TransformerInterface<T, R> {
    R transform(T input);
}
