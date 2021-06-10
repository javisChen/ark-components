package com.kt.toolkit.log;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logs {

    public static void debug(String s, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(s, args);
        }
    }

    public static void info(String s, Object... args) {
        log.info(s, args);
    }

    public static void error(String s, Object... args) {
        log.error(s, args);
    }

    public static void warn(String s, Object... args) {
        log.warn(s, args);
    }

    public static void trace(String s, Object... args) {
        log.trace(s, args);
    }
}
