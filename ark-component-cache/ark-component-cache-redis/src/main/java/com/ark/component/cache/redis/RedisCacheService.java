package com.ark.component.cache.redis;

import com.alibaba.fastjson2.JSON;
import com.ark.component.cache.CacheService;
import com.ark.component.cache.exception.CacheException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public <T> T executeScript(RedisScript<T> script, List<String> keys, List<Object> args) {
        return redisTemplate.execute(script, keys, args.toArray());
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
    public void sAdd(String key, Object... values) {
        try {
            redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
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
    public void hMSet(String key, Map<String, Object> value) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            operations.putAll(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void hMSet(String key, Map<String, Object> value, Long expires) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            operations.putAll(key, value);
            redisTemplate.expire(key, expires, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long hIncrBy(String key, String hashField, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashField, delta);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Double hIncrBy(String key, String hashField, double delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashField, delta);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void hMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            operations.putAll(key, value);
            redisTemplate.expire(key, expires, timeUnit);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void mSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long incrBy(String key, Long value) {
        try {
            return redisTemplate.opsForValue().increment(key, value);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Long decrBy(String key, Long value) {
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
    public Object hGet(String key, String hashKey) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.get(key, hashKey);
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
    public List<Object> hMGet(String key, Collection<Object> hashKeys) {
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.multiGet(key, hashKeys);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void del(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public void del(String key) {
        redisTemplate.delete(key);
    }

}