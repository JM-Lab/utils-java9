package kr.jm.utils.flow;

import kr.jm.utils.helper.JMLog;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.Flow;

public class SingleTransformProcessor<T, R> implements Flow.Processor<T, R> {
    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(SingleTransformProcessor.class);

    private SingleSubscriber<T> singleSubscriber;
    private Flow.Subscriber<? super R> subscriber;

    public SingleTransformProcessor(TransformerInterface<T, R>
            transformProcessFunction) {
        this.singleSubscriber = SingleSubscriber.build(t -> Optional
                .ofNullable(subscriber)
                .ifPresentOrElse(
                        s -> s.onNext(transformProcessFunction.transform(t)),
                        () -> log.warn("No Subscriber !!! subscriber = {}",
                                subscriber)));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        singleSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T item) {singleSubscriber.onNext(item);}

    @Override
    public void onError(Throwable throwable) {
        singleSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {singleSubscriber.onComplete();}

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {
        setSubscriber(subscriber);
    }

    public void setSubscriber(Flow.Subscriber<? super R> subscriber) {
        JMLog.info(log, "setSubscriber", subscriber);
        this.subscriber = subscriber;
    }
}
