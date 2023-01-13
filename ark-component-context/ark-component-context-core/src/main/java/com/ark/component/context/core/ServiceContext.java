package com.ark.component.context.core;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ark.component.context.core.contants.ContextConst.LOGIN_USER_CONTEXT_KEY;
import static com.ark.component.context.core.contants.ContextConst.TRACE_ID_KEY;

/**
 * 服务上下文
 * 在ServiceContextInterceptor进行初始化
 */
public class ServiceContext {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL
            = ThreadLocal.withInitial(() -> new ConcurrentHashMap<>(16));

    public static void clearContext() {
        if (THREAD_LOCAL.get() != null) {
            THREAD_LOCAL.get().clear();
        }
    }

    public static void setContext(String key, Object value) {
        Map<String, Object> contextMap = THREAD_LOCAL.get();
        contextMap.put(key, value);
    }


    public static LoginUserContext getCurrentUser() {
        Map<String, Object> context = getContext();
        if (MapUtils.isNotEmpty(context)) {
            return (LoginUserContext) context.get(LOGIN_USER_CONTEXT_KEY);
        }
        return null;
    }

    public static String getTraceId() {
        Map<String, Object> context = getContext();
        if (MapUtils.isNotEmpty(context)) {
            return (String) context.get(TRACE_ID_KEY);
        }
        return null;
    }

    public static Map<String, Object> getContext() {
        return THREAD_LOCAL.get();
    }
}
