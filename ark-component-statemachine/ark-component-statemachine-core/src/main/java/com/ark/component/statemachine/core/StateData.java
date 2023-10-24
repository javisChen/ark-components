package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.Map;

@Data
public class StateData<S> {

    private String machineId;

    private String id;

    private State<S> state;

    private Map<Object, Object> variables;

}