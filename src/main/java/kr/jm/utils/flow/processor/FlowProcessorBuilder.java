package kr.jm.utils.flow.processor;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;

public class FlowProcessorBuilder {
    public static <T, M, R> Processor<T, R> combine(
            Processor<T, M> processor1, Processor<M, R> processor2) {
        processor1.subscribe(processor2);
        return new Processor<>() {
            @Override
            public void subscribe(Flow.Subscriber<? super R> subscriber) {
                processor2.subscribe(subscriber);
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                processor1.onSubscribe(subscription);
            }

            @Override
            public void onNext(T item) {processor1.onNext(item);}

            @Override
            public void onError(Throwable throwable) {
                processor1.onError(throwable);
            }

            @Override
            public void onComplete() {processor1.onComplete();}
        };
    }

}
