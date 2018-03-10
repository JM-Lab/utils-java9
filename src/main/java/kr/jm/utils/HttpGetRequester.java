package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.helper.JMJson;

/**
 * The type Http get requester.
 */
public class HttpGetRequester {

    /**
     * Gets rest api response as object.
     *
     * @param <T>           the type parameter
     * @param uri           the uri
     * @param typeReference the type reference
     * @return the rest api response as object
     */
    public static <T> T getRestApiResponseAsObject(String uri,
            TypeReference<T> typeReference) {
        return JMJson.withJsonResource(getResponseAsString(uri), typeReference);
    }

    /**
     * Gets response as string.
     *
     * @param uri the uri
     * @return the response as string
     */
    public static String getResponseAsString(String uri) {
        return HttpRequester.getResponseAsString(uri);
    }

    /**
     * Gets response as string.
     *
     * @param uri         the uri
     * @param charsetName the charset name
     * @return the response as string
     */
    public static String getResponseAsString(String uri, String charsetName) {
        return HttpRequester.getResponseAsString(uri, charsetName);
    }
}
