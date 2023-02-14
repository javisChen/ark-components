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

    /**
     * 同时写入多个key
     */
    void multiSet(Map<String, Object> map);

    boolean set(String key, Object value, Long expires);

    boolean set(String key, Object value, Long expires, TimeUnit timeUnit);

    Object get(String key);

    void remove(String... keys);
}