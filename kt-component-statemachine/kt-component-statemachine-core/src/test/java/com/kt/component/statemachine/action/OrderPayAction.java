package com.kt.component.statemachine.action;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.action.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPayAction implements Action {

    @Override
    public void execute(StateMachineContext context) {
        System.out.println("订单支付");
    }
}
