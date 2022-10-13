package com.ark.component.statemachine.core.guard;

import com.ark.component.statemachine.core.StateMachineContext;

/**
 * 状态机执行守卫
 * @author jc
 */
public interface Guard {

    void evaluate(StateMachineContext context);
}
