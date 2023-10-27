package com.ark.component.statemachine;

import com.ark.component.statemachine.action.OrderCreateAction;
import com.ark.component.statemachine.core.StateMachine;
import com.ark.component.statemachine.core.builder.StateMachineBuilder;
import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.persist.JdbcStateMachinePersist;
import com.ark.component.statemachine.guard.OrderCreateGuard;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.EnumSet;

public class StateMachineBuilderTest {

    private final DataSource dataSource = DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3306/trade")
            .username("root")
            .password("root")
            .build();

    @Test
    public void test_build() {
        StateMachineBuilder<OrderState, OrderEvent> builder = StateMachineBuilder.newBuilder();
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
                .withTransition()
                    .source(OrderState.WAIT_RECEIVE)
                    .event(OrderEvent.RECEIVE)
                    .target(OrderState.WAIT_EVALUATE)
                    .guards(Lists.newArrayList(new OrderCreateGuard()))
                    .actions(Lists.newArrayList(new OrderCreateAction()))
                    .and()
                .withTransition()
                    .source(OrderState.WAIT_EVALUATE)
                    .event(OrderEvent.EVALUATE)
                    .target(OrderState.COMPLETED)
                    .guards(Lists.newArrayList(new OrderCreateGuard()))
                    .actions(Lists.newArrayList(new OrderCreateAction()))
                    .and()
                .build();
        //
        String orderId = "order004";
        tradeOrderStm.init(orderId, OrderEvent.CREATE);
        tradeOrderStm.sendEvent(orderId, OrderEvent.EVALUATE, new OrderCreateReq());


        // Type aClass = new TypeToken<JdbcStateMachinePersist<OrderState>>() {
        // }.getType();
        // System.out.println(aClass.getTypeName());
        // System.out.println(TypeUtil.getTypeArgument(aClass));

    }

}
