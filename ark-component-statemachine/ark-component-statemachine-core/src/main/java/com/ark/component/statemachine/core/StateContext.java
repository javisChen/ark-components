package com.ark.component.statemachine.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
public class StateContext<E, P> {

    private String bizCode;

    private String bizId;

    private E event;

    private P params;

    private Map<String, Object> extras;

}
