package com.ark.component.statemachine;

import com.ark.component.statemachine.action.OrderCreateAction;
import com.ark.component.statemachine.core.SimpleStateMachine;
import com.ark.component.statemachine.core.builder.StateMachineBuilder;
import com.ark.component.statemachine.guard.OrderCreateGuard;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.EnumSet;

public class SimpleStateMachineTest {

    private final DataSource dataSource = DataSourceBuilder.create()
            .url("jdbc:mysql://localhost:3306/trade")
            .username("root")
            .password("root")
            .build();

    @Test
    public void test_build() {
        String machineId = "trade_order";
        SimpleStateMachine<OrderState, OrderEvent> tradeOrderStm = StateMachineBuilder
                .<OrderState, OrderEvent>newBuilder()
                // 状态机基本配置
                .withConfiguration(configurationBuilder -> configurationBuilder
                        .machineId(machineId)
                        // .persist(new JdbcStateMachinePersist<>(new JdbcTemplate(dataSource)))
                        // .lock(new DefaultStateMachineLock<>())
                )
                // 状态配置
                .withStates(stateBuilder -> stateBuilder
                        .states(EnumSet.allOf(OrderState.class))
                        .initial(OrderState.WAIT_PAY)
                        .end(OrderState.COMPLETED))
                // 流转配置
                .withTransition(transitionBuilder -> transitionBuilder
                        .withExternal()
                            .source(OrderState.WAIT_PAY)
                            .event(OrderEvent.PAY)
                            .target(OrderState.WAIT_DELIVER)
                            .guards(Lists.newArrayList(new OrderCreateGuard()))
                            .actions(Lists.newArrayList(new OrderCreateAction()))
                        .and().withExternal()
                            .source(OrderState.WAIT_DELIVER)
                            .event(OrderEvent.DELIVER)
                            .target(OrderState.WAIT_RECEIVE)
                            .guards(Lists.newArrayList(new OrderCreateGuard()))
                            .actions(Lists.newArrayList(new OrderCreateAction()))
                        .and().withExternal()
                            .source(OrderState.WAIT_RECEIVE)
                            .event(OrderEvent.RECEIVE)
                            .target(OrderState.WAIT_EVALUATE)
                            .guards(Lists.newArrayList(new OrderCreateGuard()))
                            .actions(Lists.newArrayList(new OrderCreateAction()))
                        .and().withExternal()
                            .source(OrderState.WAIT_EVALUATE)
                            .event(OrderEvent.EVALUATE)
                            .target(OrderState.COMPLETED)
                            .guards(Lists.newArrayList(new OrderCreateGuard()))
                            .actions(Lists.newArrayList(new OrderCreateAction()))
                ).build();

        tradeOrderStm.nextState(OrderState.WAIT_PAY, OrderEvent.PAY, new OrderCreateReq());

    }

}
