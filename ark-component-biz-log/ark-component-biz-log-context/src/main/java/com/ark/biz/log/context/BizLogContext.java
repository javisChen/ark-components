package com.ark.biz.log.context;

import java.util.HashMap;
import java.util.Map;

public class BizLogContext {

    private static final InheritableThreadLocal<Map<String, Object>> CONTEXT = new InheritableThreadLocal<>();

    public static void addVariables(String key, Object value) {
        Object map = CONTEXT.get();
        if (map == null) {
            CONTEXT.set(new HashMap<>(16));
        }
        CONTEXT.get().put(key, value);

    }

    public static Object getVariable(String key) {
        return CONTEXT.get().get(key);
    }
    public static Map<String, Object> getVariables() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

}
