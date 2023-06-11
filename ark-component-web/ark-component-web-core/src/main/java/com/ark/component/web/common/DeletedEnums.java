package com.ark.component.web.common;

import lombok.Getter;

@Getter
public enum DeletedEnums {

    NOT(0L),
    YET(1L);

    private final Long code;

    DeletedEnums(Long code) {
        this.code = code;
    }
}
