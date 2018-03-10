package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMOptional;
import kr.jm.utils.helper.JMRestfulResource;

import java.util.Optional;
import java.util.function.Consumer;

import static kr.jm.utils.helper.JMPredicate.getEquals;
import static kr.jm.utils.helper.JMPredicate.peek;

public class RestfulResourceUpdater<T> {

    private String restfulResourceUrl;
    private TypeReference<T> type;
    private String cachedJsonString;
    private T cachedResource;

    public RestfulResourceUpdater(String restfulResourceUrl,
            TypeReference<T> type) {
        super();
        this.restfulResourceUrl = restfulResourceUrl;
        this.type = type;
    }

    public Optional<T> updateResource() {
        return JMOptional.getOptional(JMRestfulResource
                .getStringWithRestOrClasspathOrFilePath(restfulResourceUrl))
                .filter(getEquals(cachedJsonString).negate())
                .filter(peek(this::setJsonStringCache))
                .map(jsonString -> JMJson.withJsonString(jsonString, type))
                .filter(peek(resource -> this.cachedResource = resource));
    }

    public void updateResource(Consumer<T> updateConsumer) {
        updateResource().ifPresent(updateConsumer);
    }

    private void setJsonStringCache(String jsonStringCache) {
        this.cachedJsonString = jsonStringCache;
    }

    public String getRestfulResourceUrl() {
        return restfulResourceUrl;
    }

    public TypeReference<T> getType() {
        return type;
    }

    public String getCachedJsonString() {
        return cachedJsonString;
    }

    public T getCachedResource() {
        return cachedResource;
    }
}
