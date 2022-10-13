package com.ark.component.statemachine.action;

import com.ark.component.statemachine.core.StateMachineContext;
import com.ark.component.statemachine.core.action.DefaultAction;
import com.ark.component.statemachine.core.dto.OrderCreateDTO;
import com.ark.component.statemachine.dao.entity.OrderDO;
import com.ark.component.statemachine.dao.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateAction extends DefaultAction<OrderCreateDTO> {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void execute(StateMachineContext context, OrderCreateDTO params) {
        OrderDO entity = new OrderDO();
        entity.setPrice(888);
        orderMapper.insert(entity);
        System.out.println("订单创建成功");
    }
}
