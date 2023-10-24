package com.ark.component.statemachine.guard;

import com.ark.component.statemachine.core.OrderEvent;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.guard.Guard;

public abstract class SimpleGuard<P> implements Guard<OrderEvent> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluate(StateContext<OrderEvent> context) {
        return evaluate(context, ((P) context.getParams()));
    }

    protected abstract boolean evaluate(StateContext<OrderEvent> context, P params);
}
