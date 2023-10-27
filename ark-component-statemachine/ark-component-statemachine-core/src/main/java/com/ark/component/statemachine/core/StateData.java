package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.Map;

@Data
public class StateData {

    private Long id;

    private String machineId;

    private String bizId;

    private String state;

    private boolean ended;

    private Map<Object, Object> extras;


}