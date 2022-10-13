package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateMachineContext;

/**
 * 状态机
 */
public interface Action {

    void execute(StateMachineContext context);

}
