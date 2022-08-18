package com.kt.component.statemachine.guard;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.guard.Guard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPayGuard implements Guard {

    @Override
    public void evaluate(StateMachineContext context) {
        log.info("订单支付守卫");
    }

}
