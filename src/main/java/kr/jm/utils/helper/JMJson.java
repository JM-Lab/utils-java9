package kr.jm.utils.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.jm.utils.exception.JMExceptionManager;
import org.slf4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * The Class JMJson.
 */
public class JMJson {
    private static final Logger log =
            org.slf4j.LoggerFactory.getLogger(JMJson.class);
    public static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE =
            getTypeReference();
    public static final TypeReference<List<Object>> LIST_TYPE_REFERENCE =
            getTypeReference();
    public static final TypeReference<List<Map<String, Object>>>
            LIST_MAP_TYPE_REFERENCE = getTypeReference();
    private static ObjectMapper jsonMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

    /**
     * To json string.
     *
     * @param <D>        the generic type
     * @param dataObject the metric object
     * @return the string
     */
    public static <D> String toJsonString(D dataObject) {
        try {
            return jsonMapper.writeValueAsString(dataObject);
        } catch (JsonProcessingException e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonString", dataObject);
        }
    }

    /**
     * To json string or null.
     *
     * @param <D>        the generic type
     * @param dataObject the metric object
     * @return the string
     */
    public static <D> String toJsonStringOrNull(D dataObject) {
        try {
            return jsonMapper.writeValueAsString(dataObject);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * To json string.
     *
     * @param jsonFile the json file
     * @return the string
     */
    public static String toJsonString(File jsonFile) {
        return JMFiles.readString(jsonFile);
    }

    /**
     * To json file.
     *
     * @param jsonString     the json string
     * @param returnJsonFile the return json file
     * @return the file
     */
    public static File toJsonFile(String jsonString, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, jsonString);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", jsonString);
        }
    }

    /**
     * To json file or null.
     *
     * @param jsonString     the json string
     * @param returnJsonFile the return json file
     * @return the file
     */
    public static File toJsonFileOrNull(String jsonString,
            File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, jsonString);
            return returnJsonFile;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * To json file.
     *
     * @param <D>            the generic type
     * @param dataObject     the metric object
     * @param returnJsonFile the return json file
     * @return the file
     */
    public static <D> File toJsonFile(D dataObject, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, dataObject);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", dataObject);
        }
    }

    /**
     * To json file or null.
     *
     * @param <D>            the generic type
     * @param dataObject     the metric object
     * @param returnJsonFile the return json file
     * @return the file
     */
    public static <D> File toJsonFileOrNull(D dataObject, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, dataObject);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", dataObject);
        }
    }

    /**
     * From bytes.
     *
     * @param <T>           the generic type
     * @param bytes         the bytes
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withBytes(byte[] bytes,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(bytes, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withBytes", new String(bytes));
        }
    }

    /**
     * From bytes or null.
     *
     * @param <T>           the generic type
     * @param bytes         the bytes
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withBytesOrNull(byte[] bytes,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(bytes, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From bytes.
     *
     * @param <T>   the generic type
     * @param bytes the bytes
     * @param c     the c
     * @return the t
     */
    public static <T> T withBytes(byte[] bytes, Class<T> c) {
        try {
            return jsonMapper.readValue(bytes, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withBytes", new String(bytes));
        }
    }

    /**
     * From bytes or null.
     *
     * @param <T>   the generic type
     * @param bytes the bytes
     * @param c     the c
     * @return the t
     */
    public static <T> T withBytesOrNull(byte[] bytes, Class<T> c) {
        try {
            return jsonMapper.readValue(bytes, c);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From json string.
     *
     * @param <T>           the generic type
     * @param jsonString    the json string
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonString(String jsonString,
            TypeReference<T> typeReference) {
        return withBytes(jsonString.getBytes(), typeReference);
    }

    public static Map<String, Object> toMap(String jsonObjectString) {
        return withJsonString(jsonObjectString, MAP_TYPE_REFERENCE);
    }

    public static List<Object> toList(String jsonListString) {
        return withJsonString(jsonListString, LIST_TYPE_REFERENCE);
    }

    public static List<Map<String, Object>> toMapList(
            String jsonMapListString) {
        return withJsonString(jsonMapListString, LIST_MAP_TYPE_REFERENCE);
    }

    /**
     * From json string or null.
     *
     * @param <T>           the generic type
     * @param jsonString    the json string
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonStringOrNull(String jsonString,
            TypeReference<T> typeReference) {
        return withBytesOrNull(jsonString.getBytes(), typeReference);
    }

    /**
     * From json string.
     *
     * @param <T>        the generic type
     * @param jsonString the json string
     * @param c          the c
     * @return the t
     */
    public static <T> T withJsonString(String jsonString, Class<T> c) {
        return withBytes(jsonString.getBytes(), c);
    }

    /**
     * From json string or null.
     *
     * @param <T>        the generic type
     * @param jsonString the json string
     * @param c          the c
     * @return the t
     */
    public static <T> T withJsonStringOrNull(String jsonString, Class<T> c) {
        return withBytesOrNull(jsonString.getBytes(), c);
    }

    /**
     * From json file.
     *
     * @param <T>           the generic type
     * @param jsonFile      the json file
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonFile(File jsonFile,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(jsonFile, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonFile", jsonFile);
        }
    }

    /**
     * From json file or null.
     *
     * @param <T>           the generic type
     * @param jsonFile      the json file
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonFileOrNull(File jsonFile,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(jsonFile, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From json file.
     *
     * @param <T>      the generic type
     * @param jsonFile the json file
     * @param c        the c
     * @return the t
     */
    public static <T> T withJsonFile(File jsonFile, Class<T> c) {
        try {
            return jsonMapper.readValue(jsonFile, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonFile", jsonFile);
        }
    }

    /**
     * From json file or null.
     *
     * @param <T>      the generic type
     * @param jsonFile the json file
     * @param c        the c
     * @return the t
     */
    public static <T> T withJsonFileOrNull(File jsonFile, Class<T> c) {
        try {
            return jsonMapper.readValue(jsonFile, c);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From json input stream.
     *
     * @param <T>           the generic type
     * @param inputStream   the input stream
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonInputStream(InputStream inputStream,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonInputStream", inputStream);
        }
    }

    /**
     * From json input stream or null.
     *
     * @param <T>           the generic type
     * @param inputStream   the input stream
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T withJsonInputStreamOrNull(InputStream inputStream,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From json input stream.
     *
     * @param <T>         the generic type
     * @param inputStream the input stream
     * @param c           the c
     * @return the t
     */
    public static <T> T withJsonInputStream(InputStream inputStream,
            Class<T> c) {
        try {
            return jsonMapper.readValue(inputStream, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonInputStream", inputStream);
        }
    }

    /**
     * From json input stream or null.
     *
     * @param <T>         the generic type
     * @param inputStream the input stream
     * @param c           the c
     * @return the t
     */
    public static <T> T withJsonInputStreamOrNull(InputStream inputStream,
            Class<T> c) {
        try {
            return jsonMapper.readValue(inputStream, c);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * From json resource.
     *
     * @param <T>               the generic type
     * @param resourceClasspath the resource in classpath
     * @param typeReference     the type reference
     * @return the t
     */
    public static <T> T withJsonResource(String resourceClasspath,
            TypeReference<T> typeReference) {
        return withJsonInputStream(
                JMResources.getResourceInputStream(resourceClasspath),
                typeReference);
    }

    /**
     * From json resource.
     *
     * @param <T>               the generic type
     * @param resourceClasspath the resource in classpath
     * @param c                 the c
     * @return the t
     */
    public static <T> T withJsonResource(String resourceClasspath,
            Class<T> c) {
        return withJsonInputStream(
                JMResources.getResourceInputStream(resourceClasspath), c);
    }

    /**
     * From rest or classpath or file path.
     *
     * @param <T>                                  the generic type
     * @param resourceRestUrlOrClasspathOrFilePath the resource in rest url or classpath or file path
     * @param typeReference                        the type reference
     * @return the t
     */
    public static <T> T withRestOrClasspathOrFilePath(
            String resourceRestUrlOrClasspathOrFilePath,
            TypeReference<T> typeReference) {
        return withJsonString(
                JMRestfulResource.getStringWithRestOrClasspathOrFilePath(
                        resourceRestUrlOrClasspathOrFilePath),
                typeReference);
    }

    public static <T> T withRestOrFilePathOrClasspath(
            String resourceRestOrFilePathOrClasspath,
            TypeReference<T> typeReference) {
        return withJsonString(
                JMRestfulResource.getStringWithRestOrFilePathOrClasspath(
                        resourceRestOrFilePathOrClasspath),
                typeReference);
    }

    /**
     * From classpath or file path.
     *
     * @param <T>                         the generic type
     * @param resourceClasspathOrFilePath the resource in classpath or file path
     * @param typeReference               the type reference
     * @return the t
     */
    public static <T> T withClasspathOrFilePath(
            String resourceClasspathOrFilePath,
            TypeReference<T> typeReference) {
        return withJsonString(JMResources.getStringWithClasspathOrFilePath(
                resourceClasspathOrFilePath), typeReference);
    }

    public static <T> T withFilePathOrClasspath(
            String resourceFilePathOrClasspath,
            TypeReference<T> typeReference) {
        return withJsonString(JMResources
                        .getStringWithFilePathOrClasspath(resourceFilePathOrClasspath),
                typeReference);
    }

    /**
     * Convert map.
     *
     * @param <T>      the generic type
     * @param pojoBean the pojo bean
     * @return the hash map
     */
    public static <T> Map<String, Object> convertMap(T pojoBean) {
        return transform(pojoBean, MAP_TYPE_REFERENCE);
    }

    public static <T1, T2> T2 transform(T1 object,
            TypeReference<T2> transformTypeReference) {
        return withJsonString(toJsonString(object), transformTypeReference);
    }

    public static <T> TypeReference<T> getTypeReference() {
        return new TypeReference<>() {};
    }
}
