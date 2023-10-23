package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateMachineContext;

public interface StateMachinePersist<S, E, T> {

	void write(StateMachineContext<S, E, T> context);

	StateMachineContext<S, E, T> read(String id);

}
