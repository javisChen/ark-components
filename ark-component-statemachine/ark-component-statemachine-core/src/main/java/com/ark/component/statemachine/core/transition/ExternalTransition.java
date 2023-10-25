package com.ark.component.statemachine.core.transition;


import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;

public class ExternalTransition<S, E> extends AbstractTransition<S, E> {

    public ExternalTransition(State<S> source,
                              State<S> target,
                              Collection<Guard<E>> guards,
                              Collection<Action<E>> actions,
                              Trigger<S, E> trigger,
                              String name) {
        super(source, target, TransitionKind.EXTERNAL, guards, actions, trigger, name);
    }
}
