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

    public static <D> String toJsonString(D dataObject) {
        try {
            return jsonMapper.writeValueAsString(dataObject);
        } catch (JsonProcessingException e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonString", dataObject);
        }
    }

    public static <D> String toJsonStringOrNull(D dataObject) {
        try {
            return jsonMapper.writeValueAsString(dataObject);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toJsonString(File jsonFile) {
        return JMFiles.readString(jsonFile);
    }

    public static File toJsonFile(String jsonString, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, jsonString);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", jsonString);
        }
    }

    public static File toJsonFileOrNull(String jsonString,
            File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, jsonString);
            return returnJsonFile;
        } catch (Exception e) {
            return null;
        }
    }

    public static <D> File toJsonFile(D dataObject, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, dataObject);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", dataObject);
        }
    }

    public static <D> File toJsonFileOrNull(D dataObject, File returnJsonFile) {
        try {
            jsonMapper.writeValue(returnJsonFile, dataObject);
            return returnJsonFile;
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "toJsonFile", dataObject);
        }
    }

    public static <T> T withBytes(byte[] bytes,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(bytes, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withBytes", new String(bytes));
        }
    }

    public static <T> T withBytesOrNull(byte[] bytes,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(bytes, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withBytes(byte[] bytes, Class<T> c) {
        try {
            return jsonMapper.readValue(bytes, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withBytes", new String(bytes));
        }
    }

    public static <T> T withBytesOrNull(byte[] bytes, Class<T> c) {
        try {
            return jsonMapper.readValue(bytes, c);
        } catch (Exception e) {
            return null;
        }
    }

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

    public static <T> T withJsonStringOrNull(String jsonString,
            TypeReference<T> typeReference) {
        return withBytesOrNull(jsonString.getBytes(), typeReference);
    }

    public static <T> T withJsonString(String jsonString, Class<T> c) {
        return withBytes(jsonString.getBytes(), c);
    }

    public static <T> T withJsonStringOrNull(String jsonString, Class<T> c) {
        return withBytesOrNull(jsonString.getBytes(), c);
    }

    public static <T> T withJsonFile(File jsonFile,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(jsonFile, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonFile", jsonFile);
        }
    }

    public static <T> T withJsonFileOrNull(File jsonFile,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(jsonFile, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withJsonFile(File jsonFile, Class<T> c) {
        try {
            return jsonMapper.readValue(jsonFile, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonFile", jsonFile);
        }
    }

    public static <T> T withJsonFileOrNull(File jsonFile, Class<T> c) {
        try {
            return jsonMapper.readValue(jsonFile, c);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withJsonInputStream(InputStream inputStream,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonInputStream", inputStream);
        }
    }

    public static <T> T withJsonInputStreamOrNull(InputStream inputStream,
            TypeReference<T> typeReference) {
        try {
            return jsonMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withJsonInputStream(InputStream inputStream,
            Class<T> c) {
        try {
            return jsonMapper.readValue(inputStream, c);
        } catch (Exception e) {
            return JMExceptionManager.handleExceptionAndReturnNull(log, e,
                    "withJsonInputStream", inputStream);
        }
    }

    public static <T> T withJsonInputStreamOrNull(InputStream inputStream,
            Class<T> c) {
        try {
            return jsonMapper.readValue(inputStream, c);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T withJsonResource(String resourceClasspath,
            TypeReference<T> typeReference) {
        return withJsonInputStream(
                JMResources.getResourceInputStream(resourceClasspath),
                typeReference);
    }

    public static <T> T withJsonResource(String resourceClasspath,
            Class<T> c) {
        return withJsonInputStream(
                JMResources.getResourceInputStream(resourceClasspath), c);
    }

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
