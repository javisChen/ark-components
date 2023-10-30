package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.StateData;

public interface StateMachinePersist<S, E> {

	StateData read(String machineId, String bizId);

	void write(StateData stateData, StateContext<S, E> stateContext);
}
