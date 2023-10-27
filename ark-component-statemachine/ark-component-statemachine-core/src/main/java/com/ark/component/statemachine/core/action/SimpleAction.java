package com.ark.component.statemachine.core.action;

import com.ark.component.statemachine.core.StateContext;

public abstract class SimpleAction<S, E, P> implements Action<S, E> {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(StateContext<S, E> context) {
        execute(context, ((P) context.getParams()));
    }

    protected abstract void execute(StateContext<S, E> context, P params);
}
