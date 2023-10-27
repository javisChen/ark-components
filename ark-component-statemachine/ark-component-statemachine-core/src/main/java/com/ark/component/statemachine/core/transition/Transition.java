package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.trigger.Trigger;

public interface Transition<S, E> {

	<P>boolean executeGuards(StateContext<S, E> stateContext);

	<P> void executeActions(StateContext<S, E> stateContext);

	State<S> getSource();

	State<S> getTarget();

	// Collection<Guard<S, E>> getGuards();
	//
	// Collection<Action<S, E>> getActions();

	Trigger<E> getTrigger();

	// TransitionKind getKind();

	String getName();

}
