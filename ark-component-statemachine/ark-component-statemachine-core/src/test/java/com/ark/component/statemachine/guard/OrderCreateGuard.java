package com.ark.component.statemachine.guard;

import com.ark.component.statemachine.OrderCreateReq;
import com.ark.component.statemachine.core.OrderEvent;
import com.ark.component.statemachine.core.StateContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderCreateGuard extends SimpleGuard<OrderCreateReq> {

    @Override
    protected boolean evaluate(StateContext<OrderEvent> context, OrderCreateReq params) {
        log.info("{} passed", this);
        return true;
    }

}
