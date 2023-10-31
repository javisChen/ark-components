package com.ark.component.statemachine.core.transition;


import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;

import java.util.Collection;

public class ExternalTransition<S, E> extends AbstractTransition<S, E> {

    public ExternalTransition(State<S> source,
                              State<S> target,
                              Collection<Guard<S, E>> guards,
                              Collection<Action<S, E>> actions,
                              Event<E> event,
                              String name) {
        super(source, target, TransitionKind.EXTERNAL, guards, actions, event, name);
    }
}
