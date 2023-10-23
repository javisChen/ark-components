package com.ark.component.statemachine.core.transition;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;

public abstract class AbstractExternalTransition<S, E, T> extends AbstractTransition<S, E, T> implements Transition<S, E, T> {

    protected AbstractExternalTransition(State<S> target, Collection<Action<S, E, T>> actions, State<S> source,
                                         TransitionKind kind, Guard<S, E, T> guard, Trigger<S, E> trigger, String name) {
        super(target, actions, source, kind, guard, trigger, name);
    }
}
