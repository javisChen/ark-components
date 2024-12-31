package com.ark.component.context.core;

import com.ark.component.context.core.contants.ContextConstants;
import com.ark.component.security.base.user.AuthUser;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务上下文
 * 在ServiceContextInterceptor进行初始化
 */
public class ServiceContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清空上下文
     */
    public static void clearContext() {
        if (CONTEXT_THREAD_LOCAL.get() != null) {
            CONTEXT_THREAD_LOCAL.get().clear();
        }
    }

    /**
     * 添加数据到上下文
     * @param key 键
     * @param value 值
     */
    public static void addContext(String key, Object value) {
        Map<String, Object> contextHolder = CONTEXT_THREAD_LOCAL.get();
        if (contextHolder != null) {
            contextHolder.put(key, value);
        } else {
            contextHolder = new ConcurrentHashMap<>(16);
            contextHolder.put(key, value);
            CONTEXT_THREAD_LOCAL.set(contextHolder);
        }
    }

    public static AuthUser getCurrentUser() {
        Map<String, Object> context = getContext();
        return MapUtils.isNotEmpty(context)
                ? (AuthUser) context.get(ContextConstants.CONTEXT_KEY_LOGIN_USER) : null;
    }

    public static String getTraceId() {
        Map<String, Object> context = getContext();
        return MapUtils.isNotEmpty(context)
                ? (String) context.get(ContextConstants.CONTEXT_KEY_TRACE_ID) : null;
    }

    public static Map<String, Object> getContext() {
        return CONTEXT_THREAD_LOCAL.get();
    }
}
