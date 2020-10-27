package kr.jm.utils;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMString;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class RestfulResourceObjectUpdater<T> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RestfulResourceObjectUpdater.class);
    private RestfulResourceStringUpdater restfulResourceStringUpdater;
    private T cachedResource;

    public RestfulResourceObjectUpdater(String restfulResourceUrl, Function<String, T> objectMapperFunction) {
        this(restfulResourceUrl, objectMapperFunction, null);
    }

    public RestfulResourceObjectUpdater(String restfulResourceUrl, Function<String, T> objectMapperFunction,
            Consumer<T> updateConsumer) {
        this(restfulResourceUrl, 60, 0, objectMapperFunction, updateConsumer);
    }

    public RestfulResourceObjectUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis,
            Function<String, T> objectMapperFunction) {
        this(restfulResourceUrl, periodSeconds, initialDelayMillis, objectMapperFunction, null);
    }

    public RestfulResourceObjectUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis,
            Function<String, T> objectMapperFunction, Consumer<T> updateConsumer) {
        this.restfulResourceStringUpdater = new RestfulResourceStringUpdater(restfulResourceUrl, periodSeconds,
                initialDelayMillis, updateString -> updateResource(updateString, objectMapperFunction, updateConsumer));
    }


    private void updateResource(String updateString, Function<String, T> objectMapperFunction,
            Consumer<T> updateConsumer) {
        Optional.ofNullable(updateResource(updateString, objectMapperFunction)).map(o -> this.cachedResource = o)
                .filter(o -> Objects.nonNull(updateConsumer)).ifPresent(updateConsumer);
    }

    private T updateResource(String updateString, Function<String, T> objectMapperFunction) {
        try {
            return objectMapperFunction.apply(updateString);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e, "updateResource",
                    JMString.truncate(updateString, 1024, "..."));
        }
    }

    public T getCachedResource() {
        return cachedResource;
    }

    public Optional<String> updateResource() {return restfulResourceStringUpdater.updateResource();}

    public String getRestfulResourceUrl() {return restfulResourceStringUpdater.getRestfulResourceUrl();}

    public String getCachedString() {return restfulResourceStringUpdater.getCachedString();}

    public void setRestfulResourceUrl(String restfulResourceUrl) {
        this.restfulResourceStringUpdater.setRestfulResourceUrl(restfulResourceUrl);
    }

    public boolean stop(boolean mayInterruptIfRunning) {
        return restfulResourceStringUpdater.stop(mayInterruptIfRunning);
    }

    @Override
    public String toString() {
        return "RestfulJsonResourceUpdater{" + "restfulStringResourceUpdater=" + restfulResourceStringUpdater +
                ", cachedResource=" + cachedResource + '}';
    }
}