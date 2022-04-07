package com.kt.component.statemachine.action;
import java.time.LocalDateTime;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.action.DefaultAction;
import com.kt.component.statemachine.core.dto.OrderCreateDTO;
import com.kt.component.statemachine.dao.entity.OrderDO;
import com.kt.component.statemachine.dao.mapper.OrderMapper;
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
        entity.setPrice(0);
        entity.setStatus(0);
        entity.setId(0L);
//        entity.setGmtCreate(LocalDateTime.now());
        entity.setGmtModified(LocalDateTime.now());
        entity.setCreator(0L);
        entity.setModifier(0L);
        entity.setIsDeleted(0L);
        orderMapper.insert(entity);
    }
}
