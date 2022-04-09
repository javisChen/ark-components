package com.kt.component.statemachine.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态机事务操作执行
 * @author jc
 */
@Component
public class StateMachineTransactionAction {


    @Transactional
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
