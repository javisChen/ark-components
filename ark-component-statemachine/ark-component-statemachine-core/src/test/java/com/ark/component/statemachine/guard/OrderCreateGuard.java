package com.ark.component.statemachine.guard;

import com.ark.component.statemachine.OrderCreateReq;
import com.ark.component.statemachine.OrderEvent;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.guard.SimpleGuard;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderCreateGuard extends SimpleGuard<OrderEvent, OrderCreateReq> {


    @Override
    protected boolean evaluate(StateContext<OrderEvent> context, OrderCreateReq params) {
        return true;
    }
}
