package com.kt.component.common.enums;

import java.util.Arrays;
import java.util.Optional;

public class EnumUtils {

    public static <T extends BasicEnums> T getByValue(T[] values, Integer value) {
        return Arrays.stream(values)
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    public static <T extends BasicEnums> String getTextByValue(T[] values, Integer value) {
        return Optional.ofNullable(getByValue(values, value))
                .map(BasicEnums::getText)
                .orElse("");
    }
}
