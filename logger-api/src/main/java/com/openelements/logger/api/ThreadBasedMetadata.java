package com.openelements.logger.api;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ThreadBasedMetadata {

    private final ThreadLocal<Map<String, String>> metadata = new ThreadLocal<>();

    public String put(String key, String value) {
        Map<String, String> map = metadata.get();
        if (map == null) {
            metadata.set(map = new java.util.LinkedHashMap<>());
        }
        return map.put(key, value);
    }

    public Set<Entry<String, String>> entrySet() {
        Map<String, String> map = metadata.get();
        if (map != null) {
            map.entrySet();
        }
        return Set.of();
    }

    public String get(String key) {
        Map<String, String> map = metadata.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public void clear() {
        Map<String, String> map = metadata.get();
        if (map != null) {
            map.clear();
        }
    }

    public boolean containsKey(Object key) {
        Map<String, String> map = metadata.get();
        if (map == null) {
            return false;
        }
        return map.containsKey(key);
    }

}
