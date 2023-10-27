package com.ark.component.statemachine.core.guard;

import com.ark.component.statemachine.core.StateContext;

public abstract class SimpleGuard<S, E, P> implements Guard<S, E> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluate(StateContext<S, E> context) {
        return evaluate(context, ((P) context.getParams()));
    }

    protected abstract boolean evaluate(StateContext<S, E> context, P params);
}
