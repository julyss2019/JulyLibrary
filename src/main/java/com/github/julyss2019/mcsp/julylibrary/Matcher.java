package com.github.julyss2019.mcsp.julylibrary;

/**
 * 匹配器
 * @param <T>
 */
@Deprecated
public interface Matcher<T> {
    boolean match(T t);
}
