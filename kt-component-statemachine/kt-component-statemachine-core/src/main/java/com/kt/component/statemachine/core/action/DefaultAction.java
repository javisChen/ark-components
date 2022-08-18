package com.kt.component.statemachine.core.action;


import com.kt.component.statemachine.core.StateMachineContext;

public abstract class DefaultAction<T> implements Action {

    @Override
    public void execute(StateMachineContext context) {
        execute(context, (T) context.getParams());
    }

    public abstract void execute(StateMachineContext context, T params);
}
