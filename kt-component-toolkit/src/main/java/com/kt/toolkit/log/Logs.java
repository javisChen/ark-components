package com.kt.toolkit.log;

import org.slf4j.Logger;

public class Logs {

    public static void debug(Logger log, String s, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug(s, args);
        }
    }
}
