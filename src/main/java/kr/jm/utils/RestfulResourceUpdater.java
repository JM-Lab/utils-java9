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
 * The typeReference Restful resource updater.
 *
 * @param <T> the typeReference parameter
 */
public class RestfulResourceUpdater<T> {

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
        super();
        this.restfulResourceUrl = restfulResourceUrl;
        this.typeReference = new TypeReference<>() {};
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
                .map(jsonString -> JMJson.withJsonString(jsonString,
                        typeReference))
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
     * Gets typeReference.
     *
     * @return the typeReference
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
