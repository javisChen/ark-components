package com.ark.component.statemachine.core.transition;


import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;

public class DefaultExternalTransition<S, E, T> extends AbstractExternalTransition<S, E, T> {

	protected DefaultExternalTransition(State<S> target, Collection<Action<S, E, T>> actions, State<S> source, TransitionKind kind, Guard<S, E, T> guard, Trigger<S, E> trigger, String name) {
		super(target, actions, source, kind, guard, trigger, name);
	}
}
