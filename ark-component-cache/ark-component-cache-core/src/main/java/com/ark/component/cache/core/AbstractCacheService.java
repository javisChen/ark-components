package com.ark.component.cache.core;

import com.ark.component.cache.core.key.CacheKeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.ark.component.cache.CacheService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 缓存服务抽象实现
 * 提供了一些基础方法的默认实现，子类只需要实现核心方法即可
 * 使用模板方法模式处理通用的预处理逻辑
 *
 * @author JC
 */
public abstract class AbstractCacheService implements CacheService, EnvironmentAware {

    private Environment environment;
    private CacheKeyGenerator keyGenerator;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // 初始化key生成器，从配置中获取应用前缀
        this.keyGenerator = CacheKeyGenerator.builder(environment)
                .appPrefix(environment.getProperty("spring.application.name"))
                .build();
    }

    /**
     * 包装缓存key
     */
    protected String wrapKey(String key) {
        return keyGenerator.generate(key);
    }

    /**
     * 默认过期时间单位
     */
    protected static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    @Override
    public void sAdd(String key, Object... values) {
        doSAdd(wrapKey(key), values);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return doSMembers(wrapKey(key));
    }

    @Override
    public void set(String key, Object value) {
        doSet(wrapKey(key), value);
    }

    @Override
    public boolean set(String key, Object value, Long expires) {
        return set(key, value, expires, DEFAULT_TIME_UNIT);
    }

    @Override
    public boolean set(String key, Object value, Long expires, TimeUnit timeUnit) {
        return doSet(wrapKey(key), value, expires, timeUnit);
    }

    @Override
    public void hMSet(String key, Map<String, Object> value) {
        doHMSet(wrapKey(key), value);
    }

    @Override
    public void hMSet(String key, Map<String, Object> value, Long expires) {
        hMSet(key, value, expires, DEFAULT_TIME_UNIT);
    }

    @Override
    public void hMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit) {
        doHMSet(wrapKey(key), value, expires, timeUnit);
    }

    @Override
    public Long hIncrBy(String key, String hashField, long delta) {
        return doHIncrBy(wrapKey(key), hashField, delta);
    }

    @Override
    public Double hIncrBy(String key, String hashField, double delta) {
        return doHIncrByDouble(wrapKey(key), hashField, delta);
    }

    @Override
    public void mSet(Map<String, Object> map) {
        // 包装所有key
        Map<String, Object> wrappedMap = map.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> wrapKey(entry.getKey()),
                Map.Entry::getValue
            ));
        doMSet(wrappedMap);
    }

    @Override
    public Long incrBy(String key, Long value) {
        return doIncrBy(wrapKey(key), value);
    }

    @Override
    public Long decrBy(String key, Long value) {
        return doDecrBy(wrapKey(key), value);
    }

    @Override
    public Object get(String key) {
        return doGet(wrapKey(key));
    }

    @Override
    public <T> T get(String key, Class<T> target) {
        Object value = doGet(wrapKey(key));
        if (value == null) {
            return null;
        }
        return doConvert(value, target);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return doHGet(wrapKey(key), hashKey);
    }

    @Override
    public List<Object> hMGet(String key, Collection<Object> hashKeys) {
        return doHMGet(wrapKey(key), hashKeys);
    }

    @Override
    public void del(Collection<String> keys) {
        // 包装所有key
        Collection<String> wrappedKeys = keys.stream()
            .map(this::wrapKey)
            .collect(Collectors.toList());
        doDel(wrappedKeys);
    }

    @Override
    public void del(String key) {
        doDel(Collections.singleton(wrapKey(key)));
    }

    @Override
    public boolean setIfAbsent(String key, Object value) {
        return doSetIfAbsent(wrapKey(key), value);
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Long expires, TimeUnit timeUnit) {
        return doSetIfAbsent(wrapKey(key), value, expires, timeUnit);
    }

    protected abstract void doSAdd(String key, Object... values);
    protected abstract Set<Object> doSMembers(String key);
    protected abstract void doSet(String key, Object value);
    protected abstract boolean doSet(String key, Object value, Long expires, TimeUnit timeUnit);
    protected abstract void doHMSet(String key, Map<String, Object> value);
    protected abstract void doHMSet(String key, Map<String, Object> value, Long expires, TimeUnit timeUnit);
    protected abstract Long doHIncrBy(String key, String hashField, long delta);
    protected abstract Double doHIncrByDouble(String key, String hashField, double delta);
    protected abstract void doMSet(Map<String, Object> map);
    protected abstract Long doIncrBy(String key, Long value);
    protected abstract Long doDecrBy(String key, Long value);
    protected abstract Object doGet(String key);
    protected abstract Object doHGet(String key, String hashKey);
    protected abstract List<Object> doHMGet(String key, Collection<Object> hashKeys);
    protected abstract void doDel(Collection<String> keys);
    protected abstract <T> T doConvert(Object value, Class<T> target);
    protected abstract boolean doSetIfAbsent(String key, Object value);
    protected abstract boolean doSetIfAbsent(String key, Object value, Long expires, TimeUnit timeUnit);
} 