package kr.jm.utils.helper;

import kr.jm.utils.datastructure.JMArrays;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Nested map helper.
 */
public class NestedMapHelper {
    /**
     * Gets or new nested map.
     *
     * @param map              the map
     * @param nestedSeriesKeys the nested series keys
     * @return the or new nested map
     */
    public static Map<String, Object> getOrNewNestedMap(Map<String, Object> map, String... nestedSeriesKeys) {
        return JMArrays.isNullOrEmpty(nestedSeriesKeys) ? map : getOrNewNestedMap(
                digNestedMap(map, nestedSeriesKeys[0]),
                getNestedSeriesKeys(nestedSeriesKeys));
    }

    private static String[] getNestedSeriesKeys(String[] nestedSeriesKeys) {
        return nestedSeriesKeys.length < 2 ? JMArrays.EMPTY_STRINGS : Arrays
                .copyOfRange(nestedSeriesKeys, 1, nestedSeriesKeys.length);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> digNestedMap(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.computeIfAbsent(key, k -> new HashMap<>());
    }
}
