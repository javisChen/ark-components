package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateContext;

public interface Action<E> {

	<P> void execute(StateContext<E, P> context);
}
