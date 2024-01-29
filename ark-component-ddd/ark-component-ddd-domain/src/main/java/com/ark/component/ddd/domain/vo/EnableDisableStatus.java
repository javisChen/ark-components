package com.ark.component.ddd.domain.vo;

import com.ark.component.exception.ExceptionFactory;
import lombok.Getter;

/**
 * 启用和禁用状态值对象
 */
@Getter
public enum EnableDisableStatus implements BaseEnum {

    ENABLED(1),
    DISABLED(2);

    private final Integer value;

    EnableDisableStatus(int value) {
        this.value = value;
    }

    public static EnableDisableStatus from(Integer value) {
        if (DISABLED.value.equals(value)) {
            return DISABLED;
        }
        if (ENABLED.value.equals(value)) {
            return ENABLED;
        }
        throw ExceptionFactory.userException("status value is wrong");
    }

}
