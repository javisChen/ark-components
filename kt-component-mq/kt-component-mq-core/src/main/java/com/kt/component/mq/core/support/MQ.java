package com.kt.component.mq.core.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MQ {

    ROCKET("BROADCASTING"),
    KAFKA("KAFKA"),
    PULSAR("PULSAR"),
    RABBIT("CLUSTERING");

    private final String name;


}
