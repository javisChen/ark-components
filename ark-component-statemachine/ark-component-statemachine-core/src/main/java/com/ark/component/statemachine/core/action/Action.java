package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateContext;

public interface Action<E> {

	void execute(StateContext<E> context);
}
