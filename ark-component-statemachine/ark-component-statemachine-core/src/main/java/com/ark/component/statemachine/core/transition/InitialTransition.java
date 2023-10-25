package com.ark.component.statemachine.core.transition;


import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;

import java.util.Collection;

public class InitialTransition<S, E> extends AbstractTransition<S, E> {

    public InitialTransition(State<S> target,
                             Collection<Guard<E>> guards,
                             Collection<Action<E>> actions,
                             String name) {
        super(null, target, TransitionKind.INITIAL, guards, actions, null, name);
    }
}
