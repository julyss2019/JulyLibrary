package com.github.julyss2019.mcsp.julylibrary.text;



import com.github.julyss2019.mcsp.julylibrary.utils.ValidateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderContainer {
    private Map<String, String> placeholderMap = new HashMap<>();

    public PlaceholderContainer() {}


    private PlaceholderContainer(@NotNull Map<String, String> placeholderMap) {
        this.placeholderMap = new HashMap<>(ValidateUtil.notNullEntry(placeholderMap, new RuntimeException("placeholderMap 不能包含空的 KV")));
    }

    public PlaceholderContainer add(@NotNull String key, @NotNull String value) {
        placeholderMap.put(key, value);
        return this;
    }

    public Map<String, String> toMap() {
        return new HashMap<>(placeholderMap);
    }

    public static PlaceholderContainer fromMap(@NotNull Map<String, String> placeholderMap) {
        return new PlaceholderContainer(placeholderMap);
    }
}
