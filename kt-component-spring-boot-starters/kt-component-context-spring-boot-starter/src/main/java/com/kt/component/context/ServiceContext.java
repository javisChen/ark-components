package com.kt.component.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务上下文
 */
public class ServiceContext {

    private static final String LOGIN_USER_CONTEXT_KEY = "LOGIN_USER_CONTEXT";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static void clearContext() {
        THREAD_LOCAL.get().remove(LOGIN_USER_CONTEXT_KEY);
    }

    public static void setContext(String key, Object value) {
        Map<String, Object> contextMap = THREAD_LOCAL.get();
        if (contextMap == null || contextMap.size() == 0 ) {
            contextMap = new ConcurrentHashMap<>(16);
            THREAD_LOCAL.set(contextMap);
        }
        THREAD_LOCAL.get().put(key, value);
    }
    public static void setLoginUserContext(LoginUserContext loginUserContext) {
        setContext(LOGIN_USER_CONTEXT_KEY, loginUserContext);
    }

    public static LoginUserContext getLoginUserContext() {
        return (LoginUserContext) THREAD_LOCAL.get().get(LOGIN_USER_CONTEXT_KEY);
    }
}
