package com.ark.component.cache.redis;

import com.alibaba.fastjson2.JSON;
import com.ark.component.cache.CacheService;
import com.ark.component.cache.exception.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public boolean set(String key, Object value, Long expires) {
        return set(key, value, expires, TimeUnit.SECONDS);
    }


    @Override
    public boolean set(String key, Object value, Long expires, TimeUnit timeUnit) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return Boolean.TRUE.equals(operations.setIfAbsent(key, value, expires, timeUnit));
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void multiSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long increment(String key, Long value) {
        try {
            return redisTemplate.opsForValue().increment(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long decrement(String key, Long value) {
        try {
            return redisTemplate.opsForValue().decrement(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Object get(String key) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            return operations.get(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> target) {
        Object result = get(key);
        return result == null ? null : JSON.to(target, result);
    }

    @Override
    public void remove(String... keys) {
        Arrays.stream(keys).forEach(this::remove);
    }

    public void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}