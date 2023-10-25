package com.ark.component.statemachine.core.guard;

import com.ark.component.statemachine.core.StateContext;

public abstract class SimpleGuard<E, P> implements Guard<E> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluate(StateContext<E> context) {
        return evaluate(context, ((P) context.getParams()));
    }

    protected abstract boolean evaluate(StateContext<E> context, P params);
}
