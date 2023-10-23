package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateMachineContext;

public interface Action<S, E, T> {

	void execute(StateMachineContext<S, E, T> context);
}
