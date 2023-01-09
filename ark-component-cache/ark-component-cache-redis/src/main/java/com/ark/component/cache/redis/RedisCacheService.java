package com.ark.component.cache.redis;

import com.ark.component.cache.CacheService;
import com.ark.component.cache.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入缓存
     */
    @Override
    public void set(String key, Object value) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * 写入缓存设置时效时间（默认，秒）
     */
    @Override
    public boolean set(String key, Object value, Long expires) {
        return set(key, value, expires, TimeUnit.SECONDS);
    }


    /**
     * 写入缓存设置时效时间
     */
    @Override
    public boolean set(String key, Object value, Long expires, TimeUnit timeUnit) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return Boolean.TRUE.equals(operations.setIfAbsent(key, value, expires, timeUnit));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * 读取缓存
     */
    @Override
    public Object get(String key) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.get(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * 批量删除对应的value
     */
    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     */
    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     */
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}