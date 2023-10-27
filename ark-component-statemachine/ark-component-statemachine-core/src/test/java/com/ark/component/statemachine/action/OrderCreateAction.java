package com.ark.component.statemachine.action;

import com.ark.component.statemachine.OrderCreateReq;
import com.ark.component.statemachine.OrderEvent;
import com.ark.component.statemachine.OrderState;
import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.action.SimpleAction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderCreateAction extends SimpleAction<OrderState, OrderEvent, OrderCreateReq> {


    @Override
    protected void execute(StateContext<OrderState, OrderEvent> context, OrderCreateReq params) {

    }

}
