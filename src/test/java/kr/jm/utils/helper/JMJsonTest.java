package kr.jm.utils.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JMJsonTest {

    @Test
    public void testWithJsonString() {
        Map.Entry<String, List<Map.Entry<String, String>>> testEntry =
                Map.entry("a", List.of(Map.entry("1", "2"), Map.entry("3", "4"),
                        Map.entry("5", "6"), Map.entry("7", "8")));
        String testString = JMJson.toJsonString(testEntry);
        System.out.println(testString);
        System.out.println(JMJson.withJsonString(testString,
                new TypeReference<Map.Entry<String, List<Map
                        .Entry<String, Object>>>>() {}));
        Assert.assertEquals(testEntry.toString(),
                JMJson.withJsonString(testString,
                        new TypeReference<Map.Entry<String, List<Map
                                .Entry<String, Object>>>>() {}).toString());

    }
}