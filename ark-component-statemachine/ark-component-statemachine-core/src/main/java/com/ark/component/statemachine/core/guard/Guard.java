package com.ark.component.statemachine.core.guard;


import com.ark.component.statemachine.core.StateMachineContext;

public interface Guard<S, E, T> {

	/**
	 * Evaluate a guard condition.
	 *
	 * @param context the state context
	 * @return true, if guard evaluation is successful, false otherwise.
	 */
	boolean evaluate(StateMachineContext<S, E, T> context);

}
