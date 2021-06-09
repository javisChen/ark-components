package com.kt.toolkit.log;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logs {

    public static void debug(String s, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(s, args);
        }
    }
}
