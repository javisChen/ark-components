package com.ark.ddd.domain.util;

import com.ark.ddd.domain.vo.BaseEnum;

import java.util.Arrays;

public class EnumUtils {

    public static <T extends BaseEnum> T getByValue(T[] values, Integer value) {
        return Arrays.stream(values)
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
