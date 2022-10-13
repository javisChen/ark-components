package com.ark.component.mq.core.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConsumeMode {

    BROADCASTING("BROADCASTING"),
    CLUSTERING("CLUSTERING");

    private final String mode;


}
