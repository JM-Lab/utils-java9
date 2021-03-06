package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMRestfulResource;
import kr.jm.utils.helper.JMThread;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static kr.jm.utils.helper.JMPredicate.getEquals;
import static kr.jm.utils.helper.JMPredicate.peek;

/**
 * The type Restful resource updater.
 *
 * @param <T> the type parameter
 */
public class RestfulResourceUpdater<T> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RestfulResourceUpdater.class);
    private String restfulResourceUrl;
    private TypeReference<T> typeReference;
    private String cachedJsonString;
    private T cachedResource;

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     */
    public RestfulResourceUpdater(String restfulResourceUrl) {
        this(restfulResourceUrl, 0);
    }

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     * @param periodSeconds      the period seconds
     */
    public RestfulResourceUpdater(String restfulResourceUrl, int periodSeconds) {
        this(restfulResourceUrl, periodSeconds, 0);
    }

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     * @param periodSeconds      the period seconds
     * @param initialDelayMillis the initial delay millis
     */
    public RestfulResourceUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis) {
        this(restfulResourceUrl, periodSeconds, initialDelayMillis, null);
    }

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     * @param periodSeconds      the period seconds
     * @param initialDelayMillis the initial delay millis
     * @param updateConsumer     the update consumer
     */
    public RestfulResourceUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis,
            Consumer<T> updateConsumer) {
        this(restfulResourceUrl, periodSeconds, initialDelayMillis, updateConsumer, new TypeReference<>() {});
    }

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     * @param periodSeconds      the period seconds
     * @param initialDelayMillis the initial delay millis
     * @param updateConsumer     the update consumer
     * @param typeReference      the type reference
     */
    public RestfulResourceUpdater(String restfulResourceUrl, int periodSeconds, long initialDelayMillis,
            Consumer<T> updateConsumer, TypeReference<T> typeReference) {
        this.restfulResourceUrl = restfulResourceUrl;
        this.typeReference = typeReference;
        if (initialDelayMillis == 0)
            update(updateConsumer);
        if (periodSeconds > 0) {
            long periodMillis = TimeUnit.SECONDS.toMillis(periodSeconds);
            JMThread.runWithScheduleAtFixedRate(initialDelayMillis > 0 ? initialDelayMillis : periodMillis,
                    periodMillis, () -> update(updateConsumer));
        }
    }

    private void update(Consumer<T> updateConsumer) {
        Optional.ofNullable(updateConsumer).ifPresentOrElse(this::updateResource, this::updateResourceWithLog);
    }

    /**
     * Update resource with log optional.
     *
     * @return the optional
     */
    public Optional<T> updateResourceWithLog() {
        Optional<T> resourceAsOpt = updateResource();
        log.info("Updated Resource - {}, url - {}", resourceAsOpt.isPresent() ? "YES" : "NO", restfulResourceUrl);
        return resourceAsOpt;
    }

    /**
     * Update resource optional.
     *
     * @return the optional
     */
    public Optional<T> updateResource() {
        try {
            return JMOptional.getOptional(JMRestfulResource.getStringWithRestOrClasspathOrFilePath(restfulResourceUrl))
                    .filter(getEquals(cachedJsonString).negate()).filter(peek(this::setJsonStringCache))
                    .map(jsonString -> JMJson.withJsonString(jsonString, typeReference))
                    .filter(peek(resource -> this.cachedResource = resource));
        } catch (Exception e) {
            return JMExceptionManager
                    .handleExceptionAndReturnEmptyOptional(log, e, "updateResource", restfulResourceUrl);
        }
    }

    /**
     * Update resource.
     *
     * @param updateConsumer the update consumer
     */
    public void updateResource(Consumer<T> updateConsumer) {
        updateResourceWithLog().ifPresent(updateConsumer);
    }

    private void setJsonStringCache(String jsonStringCache) {
        this.cachedJsonString = jsonStringCache;
    }

    /**
     * Gets restful resource url.
     *
     * @return the restful resource url
     */
    public String getRestfulResourceUrl() {
        return restfulResourceUrl;
    }

    /**
     * Gets type reference.
     *
     * @return the type reference
     */
    public TypeReference<T> getTypeReference() {
        return typeReference;
    }

    /**
     * Gets cached json string.
     *
     * @return the cached json string
     */
    public String getCachedJsonString() {
        return cachedJsonString;
    }

    /**
     * Gets cached resource.
     *
     * @return the cached resource
     */
    public T getCachedResource() {
        return cachedResource;
    }
}