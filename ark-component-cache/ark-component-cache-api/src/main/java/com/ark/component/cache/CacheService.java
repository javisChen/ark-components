//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ark.component.cache;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 定义缓存通用接口
 * @author victor
 */
public interface CacheService {


    void setAdd(String key, Object... values);

    Set<Object> setMembers(String key);

    void set(String key, Object value);

    boolean set(String key, Object value, Long expires);

    boolean set(String key, Object value, Long expires, TimeUnit timeUnit);

    void hashSet(String key, Map<String, Object> value);

    void hashSet(String key, Map<String, Object> value, Long expires);

    void hashSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit);

    void multiSet(Map<String, Object> map);

    Long increment(String key, Long value);

    Long decrement(String key, Long value);

    Object get(String key);

    <T> T get(String key, Class<T> target);

    Object hashGet(String key, String hashKey);

    List<Object> hashMultiGet(String key, Collection<Object> hashKeys);

    void remove(String... keys);
}