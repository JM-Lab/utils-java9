package kr.jm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.jm.utils.exception.JMExceptionManager;
import kr.jm.utils.helper.JMJson;
import kr.jm.utils.helper.JMString;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class HttpRequester {
    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(HttpRequester.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static CloseableHttpClient HttpClient;

    public static <T> T getRestApiResponseAsObject(String uri,
            TypeReference<T> typeReference) {
        try {
            return JMJson.withJsonString(getResponseAsString(uri),
                    typeReference);
        } catch (Exception e) {
            throw JMExceptionManager.handleExceptionAndReturnRuntimeEx(log, e,
                    "getRestApiResponseAsObject", uri, typeReference);
        }
    }

    public static String getResponseAsString(String uri) {
        return request(new HttpGet(uri));
    }

    public static String getResponseAsString(String uri, String charsetName) {
        return request(new HttpGet(uri), charsetName);
    }


    public static String getResponseAsString(URI uri, Header header) {
        return getResponseAsString(uri.toString(), header);
    }

    public static String getResponseAsString(String uri, Header header) {
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader(header);
        request(httpGet);
        return request(httpGet);
    }

    public static URI buildUri(String httpOrHttps, String host, String path,
            Map<String, String> paramMap) {
        try {
            return new URIBuilder().setScheme(httpOrHttps).setHost(host)
                    .setPath(path)
                    .setParameters(buildNameValuePareList(paramMap)).build();
        } catch (URISyntaxException e) {
            throw JMExceptionManager.handleExceptionAndReturnRuntimeEx(log, e,
                    "getResponseAsString", httpOrHttps, host, path, paramMap);
        }
    }

    public static List<NameValuePair>
    buildNameValuePareList(Map<String, String> keyValueMap) {
        return keyValueMap.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(),
                        entry.getValue()))
                .collect(toList());
    }

    public static String postResponseAsString(URI uri, Header header,
            HttpEntity httpEntity) {
        return postResponseAsString(uri.toString(), header, httpEntity);
    }

    public static String postResponseAsString(String uri, Header header,
            HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader(header);
        httpPost.setEntity(httpEntity);
        return request(httpPost);
    }

    public static StringEntity buildHttpEntity(Object object) {
        return buildHttpEntity(JMJson.toJsonString(object));
    }

    public static StringEntity buildHttpEntity(String string) {
        return buildHttpEntity(string, UTF_8);
    }

    public static StringEntity buildHttpEntity(String string, Charset charset) {
        return new StringEntity(string, charset);
    }

    public static String request(HttpUriRequest httpUriRequest) {
        return request(httpUriRequest, Charset.defaultCharset());
    }

    public static String request(HttpUriRequest httpUriRequest,
            String charSetName) {
        return request(httpUriRequest, Charset.forName(charSetName));
    }

    public static String request(HttpUriRequest httpUriRequest,
            Charset charSet) {
        try (CloseableHttpResponse response =
                getHttpClient().execute(httpUriRequest)) {
            HttpEntity httpEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300)
                return EntityUtils.toString(httpEntity, charSet);
            throw new IllegalStateException(
                    response.getStatusLine().getStatusCode() + JMString.SPACE
                            + response.getStatusLine().getReasonPhrase());
        } catch (IOException e) {
            throw JMExceptionManager.handleExceptionAndReturnRuntimeEx(log, e,
                    "request", httpUriRequest);
        }
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClient == null ? HttpClient = HttpClients.createDefault()
                : HttpClient;
    }

    public static CloseableHttpClient
    setHttpClient(CloseableHttpClient httpClient) {
        return HttpClient = httpClient;
    }

}
