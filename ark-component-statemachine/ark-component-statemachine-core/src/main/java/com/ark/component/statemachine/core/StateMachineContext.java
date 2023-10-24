package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.Map;

@Data
public class StateMachineContext<S, E, T> {

    private String machineId;
    private String id;
    private State<S> state;
    private E event;
    private T data;
    private Map<Object, Object> variables;

}