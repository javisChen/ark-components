package com.ark.component.statemachine.core.guard;


import com.ark.component.statemachine.core.StateContext;

public interface Guard<E> {

	/**
	 * Evaluate a guard condition.
	 *
	 * @param context the state context
	 * @return true, if guard evaluation is successful, false otherwise.
	 */
	<P> boolean evaluate(StateContext<E, P> context);

}
