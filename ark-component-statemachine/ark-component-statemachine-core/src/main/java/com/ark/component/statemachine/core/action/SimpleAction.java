package com.ark.component.statemachine.core.action;

import com.ark.component.statemachine.core.StateContext;

public abstract class SimpleAction<E, P> implements Action<E> {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(StateContext<E> context) {
        execute(context, ((P) context.getParams()));
    }

    protected abstract void execute(StateContext<E> context, P params);
}
