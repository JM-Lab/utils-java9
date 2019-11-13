package kr.jm.utils.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JMObjectMapperTest {

    @Test
    public void testWithJsonString() {
        Map.Entry<String, List<Map.Entry<String, String>>> testEntry =
                Map.entry("a", List.of(Map.entry("1", "2"), Map.entry("3", "4"),
                        Map.entry("5", "6"), Map.entry("7", "8")));
        JMObjectMapper jmObjectMapper = new JMObjectMapper();
        String testString = jmObjectMapper.toJsonString(testEntry);
        System.out.println(testString);
        System.out.println(jmObjectMapper.withJsonString(testString, new TypeReference<Map.Entry<String, List<Map
                .Entry<String, Object>>>>() {}));
        Assert.assertEquals(testEntry.toString(),
                jmObjectMapper.withJsonString(testString, new TypeReference<Map.Entry<String, List<Map
                        .Entry<String, Object>>>>() {}).toString());

    }

}