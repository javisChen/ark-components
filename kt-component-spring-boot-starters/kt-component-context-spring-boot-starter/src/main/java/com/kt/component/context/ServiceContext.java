package com.kt.component.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务上下文
 */
public class ServiceContext {

    public static final String LOGIN_USER_CONTEXT_KEY = "LOGIN_USER_CONTEXT";
    public static final String TRACE_ID_KEY = "TRACE_ID";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static void clearContext() {
        if (THREAD_LOCAL.get() != null) {
            THREAD_LOCAL.get().clear();
        }
    }

    public static void setContext(String key, Object value) {
        Map<String, Object> contextMap = THREAD_LOCAL.get();
        if (contextMap != null) {
            contextMap.put(key, value);
        } else {
            contextMap = new ConcurrentHashMap<>(16);
            contextMap.put(key, value);
            THREAD_LOCAL.set(contextMap);
        }
    }


    public static LoginUserContext getCurrentUser() {
        Map<String, Object> context = getContext();
        if (context != null && context.size() > 0) {
            return (LoginUserContext) context.get(LOGIN_USER_CONTEXT_KEY);
        }
        return null;
    }

    public static String getTraceId() {
        Map<String, Object> context = getContext();
        if (context != null && context.size() > 0) {
            return (String) context.get(TRACE_ID_KEY);
        }
        return null;
    }

    public static Map<String, Object> getContext() {
        return THREAD_LOCAL.get();
    }
}
