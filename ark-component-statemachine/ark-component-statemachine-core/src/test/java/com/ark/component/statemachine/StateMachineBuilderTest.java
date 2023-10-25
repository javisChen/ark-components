package com.ark.component.statemachine;

import com.ark.component.statemachine.action.OrderCreateAction;
import com.ark.component.statemachine.core.StateMachine;
import com.ark.component.statemachine.core.builder.StateMachineBuilder;
import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
import com.ark.component.statemachine.guard.OrderCreateGuard;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

public class StateMachineBuilderTest {

    @Test
    public void test_build() {

        StateMachineBuilder<OrderState, OrderEvent> builder = StateMachineBuilder.builder();
        StateMachine<OrderState, OrderEvent> tradeOrderStm = builder
                .id("trade_order")
                .initial(OrderState.WAIT_PAY)
                .end(OrderState.COMPLETED)
                .persist(new InMemoryStateMachinePersist<>())
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
        System.out.println(tradeOrderStm);

    }

}
