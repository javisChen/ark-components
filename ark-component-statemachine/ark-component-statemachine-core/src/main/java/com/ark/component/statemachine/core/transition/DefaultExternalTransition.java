package com.ark.component.statemachine.core.transition;


import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;
import java.util.List;

public class DefaultExternalTransition<S, E, T> extends AbstractExternalTransition<S, E, T> {

    public DefaultExternalTransition(State<S> source, State<S> target, TransitionKind kind, List<Guard<E>> guards, Collection<Action<E>> actions, Trigger<S, E> trigger, String name) {
        super(source, target, kind, guards, actions, trigger, name);
    }
}
