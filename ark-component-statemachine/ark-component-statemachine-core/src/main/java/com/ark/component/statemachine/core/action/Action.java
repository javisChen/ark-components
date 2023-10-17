package com.ark.component.statemachine.core.action;


import com.ark.component.statemachine.core.StateMachineContext;

/**
 * 执行接口
 */
public interface Action {

    void execute(StateMachineContext context);

}
