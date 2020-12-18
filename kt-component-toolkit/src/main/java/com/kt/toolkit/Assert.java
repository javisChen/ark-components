package com.kt.toolkit;

public class Assert {

    public static void isTrue(boolean condition, Exception e) throws Exception {
        if (condition) {
            throw e;
        }
    }

}
