package com.kt.component.statemachine.action;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.action.DefaultAction;
import com.kt.component.statemachine.core.dto.OrderPayDTO;
import com.kt.component.statemachine.dao.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPayAction extends DefaultAction<OrderPayDTO> {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void execute(StateMachineContext context, OrderPayDTO params) {
        System.out.println("订单支付ACTION");
    }
}
