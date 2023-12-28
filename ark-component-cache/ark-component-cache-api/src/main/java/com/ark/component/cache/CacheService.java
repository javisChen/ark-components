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
    void sAdd(String key, Object... values);

    Set<Object> sMembers(String key);
    void set(String key, Object value);

    boolean set(String key, Object value, Long expires);

    boolean set(String key, Object value, Long expires, TimeUnit timeUnit);

    void hMSet(String key, Map<String, Object> value);

    void hMSet(String key, Map<String, Object> value, Long expires);

    Long hIncrBy(String key, String hashField, long delta);

    Double hIncrBy(String key, String hashField, double delta);

    void hMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit);

    void mSet(Map<String, Object> map);

    Long incrBy(String key, Long value);

    Long decrBy(String key, Long value);

    Object get(String key);

    <T> T get(String key, Class<T> target);

    Object hGet(String key, String hashKey);

    List<Object> hMGet(String key, Collection<Object> hashKeys);

    void del(Collection<String> keys);

    void del(String key);
}