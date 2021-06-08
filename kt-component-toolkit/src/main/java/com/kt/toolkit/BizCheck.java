package com.kt.toolkit;

import com.kt.component.exception.BizException;

public class BizCheck {

    public static void isTrue(boolean condition, String msg) {
        if (condition) {
            throw new BizException("A0001", msg);
        }
    }

    public static void isFalse(boolean condition, String msg) {
        isTrue(!condition, msg);
    }

}

