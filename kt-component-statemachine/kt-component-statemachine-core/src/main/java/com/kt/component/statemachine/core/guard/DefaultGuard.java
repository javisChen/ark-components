package com.kt.component.statemachine.core.guard;


import com.kt.component.statemachine.core.StateMachineContext;

public abstract class DefaultGuard<T> implements Guard {

    @SuppressWarnings("unchecked")
    @Override
    public void evaluate(StateMachineContext context) {
        evaluate(context, (T) context.getParams());
    }

    public abstract void evaluate(StateMachineContext context, T params);
}
