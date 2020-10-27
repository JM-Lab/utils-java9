package kr.jm.utils;

import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMLog;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMRestfulResource;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static kr.jm.utils.helper.JMPredicate.getEquals;

public class RestfulResourceStringUpdater {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RestfulResourceStringUpdater.class);

    private String restfulResourceUrl;
    private String cachedString;
    private Consumer<String> updateConsumer;
    private ScheduledFuture<?> scheduledFuture;

    public RestfulResourceStringUpdater(String restfulResourceUrl) {
        this(restfulResourceUrl, 60);
    }

    public RestfulResourceStringUpdater(String restfulResourceUrl, Consumer<String> updateConsumer) {
        this(restfulResourceUrl, 60, 0, updateConsumer);
    }

    public RestfulResourceStringUpdater(String restfulResourceUrl, int periodSeconds) {
        this(restfulResourceUrl, periodSeconds, 0);
    }

    public RestfulResourceStringUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis) {
        this(restfulResourceUrl, periodSeconds, initialDelayMillis, null);
    }

    public RestfulResourceStringUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis,
            Consumer<String> updateConsumer) {
        this.restfulResourceUrl = restfulResourceUrl;
        if (initialDelayMillis == 0)
            updateResource(updateConsumer);
        if (periodSeconds > 0) {
            long periodMillis = TimeUnit.SECONDS.toMillis(periodSeconds);
            this.scheduledFuture =
                    JMThread.runWithScheduleAtFixedRate(initialDelayMillis > 0 ? initialDelayMillis : periodMillis,
                            periodMillis, () -> updateResource(updateConsumer));
        }
    }

    public boolean stop(boolean mayInterruptIfRunning) {
        return this.scheduledFuture.cancel(mayInterruptIfRunning);
    }

    private void updateResource(Consumer<String> updateConsumer) {
        updateResource().ifPresent(updateConsumer);
    }

    public Optional<String> updateResource() {
        try {
            return JMOptional.getOptional(JMRestfulResource.getStringWithRestOrClasspathOrFilePath(restfulResourceUrl))
                    .filter(getEquals(cachedString).negate()).map(this::updateCachedString);
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnEmptyOptional(log, e, "updateResource", restfulResourceUrl);
        }
    }

    private String updateCachedString(String cachedString) {
        JMLog.info(log, "updateCachedString", restfulResourceUrl);
        return this.cachedString = cachedString;
    }

    public String getRestfulResourceUrl() {
        return restfulResourceUrl;
    }

    public String getCachedString() {
        return cachedString;
    }

    public void setRestfulResourceUrl(String restfulResourceUrl) {
        this.restfulResourceUrl = restfulResourceUrl;
    }

    @Override
    public String toString() {
        return "RestfulStringResourceUpdater{" + "restfulResourceUrl='" + restfulResourceUrl + '\'' +
                ", cachedString='" + cachedString + '\'' + '}';
    }
}