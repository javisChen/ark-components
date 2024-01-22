package com.ark.component.ddd.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 启用和禁用状态值对象
 */
@Getter
public enum EnableDisableStatus {

    DISABLED(0),
    ENABLED(1);

    private final Integer value;

    EnableDisableStatus(int value) {
        this.value = value;
    }

    public EnableDisableStatus from(Integer value) {
        if (DISABLED.value.equals(value)) {
            return DISABLED;
        }
        if (ENABLED.value.equals(value)) {
            return ENABLED;
        }
        return null;
    }
}
