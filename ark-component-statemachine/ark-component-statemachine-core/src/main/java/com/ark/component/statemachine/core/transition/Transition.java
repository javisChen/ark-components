package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.trigger.Trigger;

public interface Transition<S, E> {

	boolean executeGuards(StateContext<S, E> stateContext);

	void executeActions(StateContext<S, E> stateContext);

	State<S> getSource();

	State<S> getTarget();

	Trigger<E> getTrigger();

	String getName();

	Event<E> getEvents();

	TransitionKind getKind();

}
