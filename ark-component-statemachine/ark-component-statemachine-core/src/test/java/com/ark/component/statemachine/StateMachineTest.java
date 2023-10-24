package com.ark.component.statemachine;

import com.ark.component.statemachine.core.*;
import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
import com.ark.component.statemachine.core.transition.DefaultExternalTransition;
import com.ark.component.statemachine.core.transition.Transition;
import com.ark.component.statemachine.core.trigger.EventTrigger;
import com.ark.component.statemachine.guard.OrderCreateGuard;
import com.google.common.collect.Lists;

import java.util.EnumSet;
import java.util.List;

public class StateMachineTest {

    public static void main(String[] args) {

        StateMachine<OrderState, OrderEvent, Object> machine = new StateMachine<>();
        machine.setId("trade_order");
        machine.setInitial(new State<>(OrderState.WAIT_PAY));
        DefaultExternalTransition<OrderState, OrderEvent, Object> initialTransition = new DefaultExternalTransition<>(null,
                new State<>(OrderState.WAIT_PAY),
                null,
                Lists.newArrayList(new OrderCreateGuard()),
                null,
                new EventTrigger<>(new Event<>(OrderEvent.CREATE)),
                "下单");
        machine.setInitialTransition(initialTransition);

        machine.setEnd(new State<>(OrderState.COMPLETED));
        EnumSet<OrderState> orderStates = EnumSet.allOf(OrderState.class);
        machine.setStates(orderStates.stream().map(State::new).toList());

        EnumSet<OrderEvent> orderEvents = EnumSet.allOf(OrderEvent.class);
        machine.setEvents(orderEvents.stream().map(Event::new).toList());

        List<Transition<OrderState, OrderEvent, Object>> transitions = Lists.newArrayList();
        DefaultExternalTransition<OrderState, OrderEvent, Object> externalTransition
                = new DefaultExternalTransition<>(new State<>(OrderState.WAIT_PAY), new State<>(OrderState.WAIT_DELIVER),
                null,
                null,
                null, new EventTrigger<>(new Event<>(OrderEvent.PAY)), "");
        transitions.add(externalTransition);
        machine.setTransitions(transitions);

        machine.setStateMachinePersist(new InMemoryStateMachinePersist<>());

        machine.setStateMachineLock(new DefaultStateMachineLock<>());

        machine.init("order001", OrderEvent.CREATE, null);

        machine.sendEvent("order001", OrderEvent.PAY, null);}

}
