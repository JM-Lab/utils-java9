package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.helper.JMJson;

public class HttpGetRequester {

    public static <T> T getRestApiResponseAsObject(String uri,
            TypeReference<T> typeReference) {
        return JMJson.withJsonResource(getResponseAsString(uri), typeReference);
    }

    public static String getResponseAsString(String uri) {
        return HttpRequester.getResponseAsString(uri);
    }

    public static String getResponseAsString(String uri, String charsetName) {
        return HttpRequester.getResponseAsString(uri, charsetName);
    }
}
