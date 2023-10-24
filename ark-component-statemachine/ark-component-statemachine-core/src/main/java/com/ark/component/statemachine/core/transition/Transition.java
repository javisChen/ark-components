package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.trigger.Trigger;

public interface Transition<S, E, T> {

	<P>boolean executeGuards(StateContext<E, P> stateContext);

	<P> void executeActions(StateContext<E, P> stateContext);

	State<S> getSource();

	State<S> getTarget();

	// Collection<Guard<E>> getGuards();
	//
	// Collection<Action<E>> getActions();

	Trigger<S, E> getTrigger();

	// TransitionKind getKind();

	String getName();

}
