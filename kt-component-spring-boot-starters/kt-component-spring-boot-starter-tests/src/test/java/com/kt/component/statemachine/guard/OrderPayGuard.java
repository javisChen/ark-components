package com.kt.component.statemachine.guard;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.guard.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderPayGuard implements Guard {

    private final Logger log = LoggerFactory.getLogger(OrderPayGuard.class);

    @Override
    public boolean evaluate(StateMachineContext context) {
        log.info("订单支付守卫");
        return true;
    }

}
