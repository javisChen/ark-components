package com.ark.component.cache.core;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ark.component.cache.CacheService;
import com.ark.component.common.util.spring.SpringUtils;

import java.lang.reflect.Type;
import java.util.function.Function;

public class CacheHelper {

    public static <T> T execute(String cacheKey, Function<String, T> function) {
        CacheService cacheService = SpringUtils.getBean(CacheService.class);
        if (cacheService != null) {
            Object cache = cacheService.get(cacheKey);
            if (cache != null) {
                Type rawType = new TypeReference<T>() {
                }.getType();
                return (T) JSON.parseArray((String) cache, rawType);
            }
        }
        T result = function.apply(cacheKey);
        cacheService.set(cacheKey, JSON.toJSONString(result));
        return result;
    }
}
