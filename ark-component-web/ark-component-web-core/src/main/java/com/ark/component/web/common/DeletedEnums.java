package com.ark.component.web.common;

import lombok.Getter;

public enum DeletedEnums {

    NOT(0L),
    YET(1L);

    @Getter
    private Long code;

    DeletedEnums(Long code) {
        this.code = code;
    }
}
