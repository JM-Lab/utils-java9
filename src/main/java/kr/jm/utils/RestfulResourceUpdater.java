package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMRestfulResource;

import java.util.Optional;
import java.util.function.Consumer;

import static kr.jm.utils.helper.JMPredicate.getEquals;
import static kr.jm.utils.helper.JMPredicate.peek;

/**
 * The type Restful resource updater.
 *
 * @param <T> the type parameter
 */
public class RestfulResourceUpdater<T> {

    private String restfulResourceUrl;
    private TypeReference<T> type;
    private String cachedJsonString;
    private T cachedResource;

    /**
     * Instantiates a new Restful resource updater.
     *
     * @param restfulResourceUrl the restful resource url
     * @param type               the type
     */
    public RestfulResourceUpdater(String restfulResourceUrl,
            TypeReference<T> type) {
        super();
        this.restfulResourceUrl = restfulResourceUrl;
        this.type = type;
    }

    /**
     * Update resource optional.
     *
     * @return the optional
     */
    public Optional<T> updateResource() {
        return JMOptional.getOptional(JMRestfulResource
                .getStringWithRestOrClasspathOrFilePath(restfulResourceUrl))
                .filter(getEquals(cachedJsonString).negate())
                .filter(peek(this::setJsonStringCache))
                .map(jsonString -> JMJson.withJsonString(jsonString, type))
                .filter(peek(resource -> this.cachedResource = resource));
    }

    /**
     * Update resource.
     *
     * @param updateConsumer the update consumer
     */
    public void updateResource(Consumer<T> updateConsumer) {
        updateResource().ifPresent(updateConsumer);
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
     * Gets type.
     *
     * @return the type
     */
    public TypeReference<T> getType() {
        return type;
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
