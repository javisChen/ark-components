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
 * 提供对缓存的基础操作，包括：
 * - 字符串操作 (set/get)
 * - Hash表操作 (hSet/hGet)
 * - 集合操作 (sAdd/sMembers)
 * - 计数操作 (incrBy/decrBy)
 * 
 * @author JC
 */
public interface CacheService {
    /**
     * 向集合中添加元素
     *
     * @param key 集合的key
     * @param values 要添加的元素，可以是一个或多个
     */
    void sAdd(String key, Object... values);

    /**
     * 获取集合中的所有元素
     *
     * @param key 集合的key
     * @return 集合中的所有元素
     */
    Set<Object> sMembers(String key);

    /**
     * 设置缓存键值对
     *
     * @param key 缓存key
     * @param value 缓存value
     */
    void set(String key, Object value);

    /**
     * 设置带过期时间的缓存键值对
     *
     * @param key 缓存key
     * @param value 缓存value
     * @param expires 过期时间(毫秒)
     * @return 是否设置成功
     */
    boolean set(String key, Object value, Long expires);

    /**
     * 设置带过期时间的缓存键值对，可指定时间单位
     *
     * @param key 缓存key
     * @param value 缓存value
     * @param expires 过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    boolean set(String key, Object value, Long expires, TimeUnit timeUnit);

    /**
     * 批量设置Hash表的键值对
     *
     * @param key Hash表的key
     * @param value 要设置的键值对Map
     */
    void hMSet(String key, Map<String, Object> value);

    /**
     * 批量设置Hash表的键值对，带过期时间
     *
     * @param key Hash表的key
     * @param value 要设置的键值对Map
     * @param expires 过期时间(毫秒)
     */
    void hMSet(String key, Map<String, Object> value, Long expires);

    /**
     * Hash表中指定字段的值增加给定的整数增量
     *
     * @param key Hash表的key
     * @param hashField Hash表中的字段名
     * @param delta 增量值(整数)
     * @return 增加后的值
     */
    Long hIncrBy(String key, String hashField, long delta);

    /**
     * Hash表中指定字段的值增加给定的浮点数增量
     *
     * @param key Hash表的key
     * @param hashField Hash表中的字段名
     * @param delta 增量值(浮点数)
     * @return 增加后的值
     */
    Double hIncrBy(String key, String hashField, double delta);

    /**
     * 批量设置Hash表的键值对，带过期时间，可指定时间单位
     *
     * @param key Hash表的key
     * @param value 要设置的键值对Map
     * @param expires 过期时间
     * @param timeUnit 时间单位
     */
    void hMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit);

    /**
     * 批量设置多个键值对
     *
     * @param map 要设置的键值对Map
     */
    void mSet(Map<String, Object> map);

    /**
     * 将key对应的值增加给定的整数增量
     *
     * @param key 缓存key
     * @param value 增量值
     * @return 增加后的值
     */
    Long incrBy(String key, Long value);

    /**
     * 将key对应的值减少给定的整数增量
     *
     * @param key 缓存key
     * @param value 减少的值
     * @return 减少后的值
     */
    Long decrBy(String key, Long value);

    /**
     * 获取缓存值
     *
     * @param key 缓存key
     * @return 缓存值
     */
    Object get(String key);

    /**
     * 获取缓存值并转换为指定类型
     *
     * @param key 缓存key
     * @param target 目标类型
     * @param <T> 泛型类型
     * @return 转换后的缓存值
     */
    <T> T get(String key, Class<T> target);

    /**
     * 获取Hash表中指定字段的值
     *
     * @param key Hash表的key
     * @param hashKey Hash表中的字段名
     * @return 字段值
     */
    Object hGet(String key, String hashKey);

    /**
     * 批量获取Hash表中多个字段的值
     *
     * @param key Hash表的key
     * @param hashKeys Hash表中的字段名集合
     * @return 字段值列表
     */
    List<Object> hMGet(String key, Collection<Object> hashKeys);

    /**
     * 批量删除缓存
     *
     * @param keys 要删除的缓存key集合
     */
    void del(Collection<String> keys);

    /**
     * 删除单个缓存
     *
     * @param key 要删除的缓存key
     */
    void del(String key);

    /**
     * 当key不存在时设置值
     *
     * @param key 缓存key
     * @param value 缓存value
     * @return true if the key was set, false if the key was not set
     */
    boolean setIfAbsent(String key, Object value);

    /**
     * 当key不存在时设置值，同时设置过期时间
     *
     * @param key 缓存key
     * @param value 缓存value
     * @param expires 过期时间
     * @param timeUnit 时间单位
     * @return true if the key was set, false if the key was not set
     */
    boolean setIfAbsent(String key, Object value, Long expires, TimeUnit timeUnit);

    /**
     * 设置缓存键值对，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key 缓存key
     * @param value 缓存value
     */
    void set(String appPrefix, String key, Object value);

    /**
     * 设置带过期时间的缓存键值对，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key 缓存key
     * @param value 缓存value
     * @param expires 过期时间(毫秒)
     * @return 是否设置成功
     */
    boolean set(String appPrefix, String key, Object value, Long expires);

    /**
     * 设置带过期时间的缓存键值对，可指定时间单位，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key 缓存key
     * @param value 缓存value
     * @param expires 过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    boolean set(String appPrefix, String key, Object value, Long expires, TimeUnit timeUnit);

    /**
     * 获取缓存值，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key 缓存key
     * @return 缓存值
     */
    Object get(String appPrefix, String key);

    /**
     * Hash表操作相关方法重载
     */
    void hMSet(String appPrefix, String key, Map<String, Object> value);
    void hMSet(String appPrefix, String key, Map<String, Object> value, Long expires);
    void hMSet(String appPrefix, String key, Map<String, Object> value, Long expires, TimeUnit timeUnit);
    Object hGet(String appPrefix, String key, String hashKey);
    List<Object> hMGet(String appPrefix, String key, Collection<Object> hashKeys);
    Long hIncrBy(String appPrefix, String key, String hashField, long delta);
    Double hIncrBy(String appPrefix, String key, String hashField, double delta);
    Long hDel(String key, Object... hashKeys);
    Long hDel(String appPrefix, String key, Object... hashKeys);

    /**
     * 集合操作相关方法重载
     */
    void sAdd(String appPrefix, String key, Object... values);
    Set<Object> sMembers(String appPrefix, String key);

    /**
     * 计数操作相关方法重载
     */
    Long incrBy(String appPrefix, String key, Long value);
    Long decrBy(String appPrefix, String key, Long value);

    /**
     * 删除操作相关方法重载
     */
    void del(String appPrefix, String key);
    void del(String appPrefix, Collection<String> keys);

    /**
     * setIfAbsent操作相关方法重载
     */
    boolean setIfAbsent(String appPrefix, String key, Object value);
    boolean setIfAbsent(String appPrefix, String key, Object value, Long expires, TimeUnit timeUnit);

    /**
     * 获取缓存值并转换为指定类型，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key 缓存key
     * @param target 目标类型
     * @param <T> 泛型类型
     * @return 转换后的缓存值
     */
    <T> T get(String appPrefix, String key, Class<T> target);

    /**
     * 获取Hash表中所有字段的值列表
     *
     * @param appPrefix 应用前缀
     * @param key Hash表的key
     * @return Hash表中所有字段对应的值列表
     */
    List<Object> hVals(String appPrefix, String key);

    /**
     * 获取Hash表中所有的字段和值
     *
     * @param key Hash表的key
     * @return Hash表中所有的字段和值的Map
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 获取Hash表中所有的字段和值，支持指定应用前缀
     *
     * @param appPrefix 应用前缀
     * @param key Hash表的key
     * @return Hash表中所有的字段和值的Map
     */
    Map<Object, Object> hGetAll(String appPrefix, String key);
}