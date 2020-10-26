package kr.jm.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.jm.utils.helper.JMObjectMapper;
import kr.jm.utils.helper.JMThread;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RestfulResourceObjectUpdaterTest {
    private List<Map<String, Object>> updateResource;
    private RestfulResourceObjectUpdater<List<Map<String, Object>>> restfulJsonResourceUpdater;

    @Before
    public void setUp() throws Exception {
        JMObjectMapper jmObjectMapper =
                new JMObjectMapper(new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                        .enable(JsonParser.Feature.ALLOW_COMMENTS));
        this.restfulJsonResourceUpdater =
                new RestfulResourceObjectUpdater<>("testRestfulJsonResource.json", 2, 0,
                        s -> jmObjectMapper.withJsonString(s, new TypeReference<>() {}), map -> updateResource = map);
        JMThread.sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
        restfulJsonResourceUpdater.stop(false);
    }

    @Test
    public void updateResource() {
        System.out.println(updateResource);
        assertEquals(restfulJsonResourceUpdater.getCachedResource().toString(), updateResource.toString());
        assertFalse(restfulJsonResourceUpdater.getCachedString().contains("PREMIUM2"));
        assertEquals(3,
                updateResource.stream().map(map -> map.get("priceType")).filter(p -> "PREMIUM".equals(p)).count());
        assertEquals(325069045,
                updateResource.stream().map(map -> map.get("maxBytes")).mapToInt(l -> (int) l).sum());

        restfulJsonResourceUpdater.setRestfulResourceUrl("testRestfulJsonResource2.json");
        JMThread.sleep(1000);
        System.out.println(updateResource);
        assertEquals(restfulJsonResourceUpdater.getCachedResource().toString(), updateResource.toString());
        assertTrue(restfulJsonResourceUpdater.getCachedString().contains("PREMIUM2"));
        assertEquals(2,
                updateResource.stream().map(map -> map.get("priceType")).filter(p -> "PREMIUM".equals(p)).count());
        assertEquals(325069046,
                updateResource.stream().map(map -> map.get("maxBytes")).mapToInt(l -> (int) l).sum());
    }
}