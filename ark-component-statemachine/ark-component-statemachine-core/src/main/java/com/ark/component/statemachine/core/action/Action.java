package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateContext;

public interface Action<S, E> {

	void execute(StateContext<S, E> context);
}
