package com.ark.component.statemachine;

import cn.hutool.core.util.TypeUtil;
import com.ark.component.statemachine.action.OrderCreateAction;
import com.ark.component.statemachine.core.StateMachine;
import com.ark.component.statemachine.core.builder.StateMachineBuilder;
import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.persist.JdbcStateMachinePersist;
import com.ark.component.statemachine.guard.OrderCreateGuard;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.util.EnumSet;

public class StateMachineBuilderTest {

    DataSource dataSource = DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3306/trade")
            .username("root")
            .password("root")
            .build();

    @Test
    public void test_build() {

        ServiceImpl<TestMapper, String> service = new ServiceImpl<>();
        System.out.println(service);

        StateMachineBuilder<OrderState, OrderEvent> builder = StateMachineBuilder.builder();

        Type typeArgument = TypeUtil.getTypeArgument(new TypeToken<StateMachineBuilder<OrderState, OrderEvent>>() {
        }.getType());
        System.out.println(typeArgument);
        StateMachine<OrderState, OrderEvent> tradeOrderStm = builder
                .id("trade_order")
                .initial(OrderState.WAIT_PAY)
                .end(OrderState.COMPLETED)
                .persist(new JdbcStateMachinePersist<>(new JdbcTemplate(dataSource)))
                .lock(new DefaultStateMachineLock<>())
                .states(EnumSet.allOf(OrderState.class))

                .withTransition()
                    .source(OrderState.WAIT_PAY)
                    .event(OrderEvent.PAY)
                    .target(OrderState.WAIT_DELIVER)
                    .guards(Lists.newArrayList(new OrderCreateGuard()))
                    .actions(Lists.newArrayList(new OrderCreateAction()))
                .and()
                .withTransition()
                    .source(OrderState.WAIT_DELIVER)
                    .event(OrderEvent.DELIVER)
                    .target(OrderState.WAIT_RECEIVE)
                    .guards(Lists.newArrayList(new OrderCreateGuard()))
                    .actions(Lists.newArrayList(new OrderCreateAction()))
                .and()
                .build();
        //
        tradeOrderStm.init("order002", OrderEvent.CREATE, new OrderCreateReq());
        tradeOrderStm.sendEvent("order002", OrderEvent.PAY, new OrderCreateReq());
        System.out.println(tradeOrderStm);


        // Type aClass = new TypeToken<JdbcStateMachinePersist<OrderState>>() {
        // }.getType();
        // System.out.println(aClass.getTypeName());
        // System.out.println(TypeUtil.getTypeArgument(aClass));

    }

}
