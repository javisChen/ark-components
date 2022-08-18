package com.kt.component.statemachine.core.action;


import com.kt.component.statemachine.core.StateMachineContext;

/**
 * 状态机
 */
public interface Action {

    void execute(StateMachineContext context);

}
