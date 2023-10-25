package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.Map;

@Data
public class StateData<S> {

    private Long id;

    private String machineId;

    private String bizId;

    private State<S> state;

    private boolean ended;

    private Map<Object, Object> extras;


}