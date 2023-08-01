//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ark.component.cache;


import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 定义缓存通用接口
 * @author victor
 */
public interface CacheService {

    /**
     * 写入单个key
     */
    void set(String key, Object value);

    boolean set(String key, Object value, Long expires);

    boolean set(String key, Object value, Long expires, TimeUnit timeUnit);

    /**
     * 同时写入多个key
     */
    void multiSet(Map<String, Object> map);

    Long increment(String key, Long value);

    Long decrement(String key, Long value);

    Object get(String key);

    <T> T get(String key, Class<T> target);

    void remove(String... keys);
}