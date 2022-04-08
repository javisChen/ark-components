package com.kt.component.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public class ParamsChecker {

    public static void throwIfIsEmpty(String str, RuntimeException exception) {
        throwIfIsTrue(StringUtils.isEmpty(str), exception);
    }

    public static void throwIfIsEmpty(Collection<?> collection, RuntimeException exception) {
        throwIfIsTrue(CollectionUtils.isEmpty(collection), exception);
    }

    public static void throwIfIsBlank(String str, RuntimeException exception) {
        throwIfIsTrue(StringUtils.isBlank(str), exception);
    }

    public static void throwIfIsNull(Object obj, RuntimeException exception) {
        throwIfIsTrue(Objects.isNull(obj), exception);
    }

    public static void throwIfIsTrue(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }
}
