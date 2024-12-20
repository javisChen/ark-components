package com.ark.component.cache.redis;

import com.alibaba.fastjson2.JSON;
import com.ark.component.cache.core.AbstractCacheService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCacheService extends AbstractCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doSAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    @Override
    protected Set<Object> doSMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    protected void doSet(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    protected boolean doSet(String key, Object value, Long expires, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expires, timeUnit);
        return true;
    }

    @Override
    protected void doHMSet(String key, Map<String, Object> value) {
        redisTemplate.opsForHash().putAll(key, value);
    }

    @Override
    protected void doHMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, value);
        redisTemplate.expire(key, expires, timeUnit);
    }

    @Override
    protected Long doHIncrBy(String key, String hashField, long delta) {
        return redisTemplate.opsForHash().increment(key, hashField, delta);
    }

    @Override
    protected Double doHIncrByDouble(String key, String hashField, double delta) {
        return redisTemplate.opsForHash().increment(key, hashField, delta);
    }

    @Override
    protected void doMSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    @Override
    protected Long doIncrBy(String key, Long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    protected Long doDecrBy(String key, Long value) {
        return redisTemplate.opsForValue().decrement(key, value);
    }

    @Override
    protected Object doGet(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    protected Object doHGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    protected List<Object> doHMGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    @Override
    protected void doDel(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    protected <T> T doConvert(Object value, Class<T> target) {
        return JSON.to(target, value);
    }

    @Override
    protected boolean doSetIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    @Override
    protected boolean doSetIfAbsent(String key, Object value, Long expires, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, expires, timeUnit));
    }
}