package com.github.julyss2019.mcsp.julylibrary.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 用于内部的占位符
 */
public class PlaceholderContainer {
    private Map<String, String> placeholderMap;

    public PlaceholderContainer() {}

    public Map<String, String> getPlaceholderMap() {
        return placeholderMap;
    }

    public PlaceholderContainer addInner(@NotNull String key, @NotNull String value) {
        return add("{" + key + "}", value);
    }

    public PlaceholderContainer addInner(@NotNull String key, int value) {
        return addInner(key, String.valueOf(value));
    }

    public PlaceholderContainer addInner(@NotNull String key, double value) {
        return addInner(key, String.valueOf(value));
    }

    public PlaceholderContainer add(@NotNull String key, double value) {
        return add(key, String.valueOf(value));
    }

    public PlaceholderContainer add(@NotNull String key, int value) {
        return add(key, String.valueOf(value));
    }

    public PlaceholderContainer add(@NotNull String key, @NotNull String value) {
        placeholderMap.put(key, value);
        return this;
    }
}
