package com.kt.component.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Checker {

    public static void throwExceptionIfIsEmpty(String str, RuntimeException exception) {
        throwExceptionIfIsTrue(StringUtils.isEmpty(str), exception);
    }

    public static void throwExceptionIfIsBlank(String str, RuntimeException exception) {
        throwExceptionIfIsTrue(StringUtils.isBlank(str), exception);
    }

    public static void throwExceptionIfIsNull(Object obj, RuntimeException exception) {
        throwExceptionIfIsTrue(Objects.isNull(obj), exception);
    }

    public static void throwExceptionIfIsTrue(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }
}
