package com.ark.component.statemachine.core.service;

public interface IStateMachineService<S, E> {

    <P> void fireEvent(String bizId, E event, P params);

}
