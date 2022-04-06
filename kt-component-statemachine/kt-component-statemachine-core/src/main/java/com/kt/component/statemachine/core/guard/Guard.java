package com.kt.component.statemachine.core.guard;

import com.kt.component.statemachine.core.StateMachineContext;

/**
 * 状态机执行守卫
 * @author jc
 */
public interface Guard {

    boolean evaluate(StateMachineContext context);
}
