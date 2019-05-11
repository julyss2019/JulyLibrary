package com.github.julyss2019.mcsp.julylibrary.map;

import java.util.HashMap;
import java.util.Map;


public class MapBuilder<K, V> {
    private Map<K, V> map = new HashMap<>();

    public MapBuilder() {}

    public MapBuilder put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
