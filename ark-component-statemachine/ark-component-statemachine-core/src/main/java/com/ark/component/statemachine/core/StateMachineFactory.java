package com.ark.component.statemachine.core;

import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.DefaultExternalTransition;
import com.ark.component.statemachine.core.transition.Transition;
import com.ark.component.statemachine.core.trigger.EventTrigger;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StateMachineFactory<S, E, T> {

    public Map<String, StateMachine<S, E, T>> machines = new HashMap<>(16);

    private final StateMachinePersist<S, E, T> stateMachinePersist = new InMemoryStateMachinePersist<>();

    public StateMachine<S, E, T> acquireStateMachine(String machineId) {
        log.info("Acquiring machine with id " + machineId);
        StateMachine<S, E, T> stateMachine;
        // naive sync to handle concurrency with release
        stateMachine = machines.get(machineId);
        if (stateMachine == null) {
            log.info("Getting new machine from factory with id " + machineId);
            stateMachine = machines.get(machineId);
            machines.put(machineId, stateMachine);
        }
        return stateMachine;
    }

    public static void main(String[] args) {
        StateMachine<OrderState, OrderEvent, Object> machine = new StateMachine<>();
        machine.setId("trade_order");
        machine.setInitial(new State<>(OrderState.WAIT_PAY));
        DefaultExternalTransition<OrderState, OrderEvent, Object> initialTransition = new DefaultExternalTransition<>(null, new State<>(OrderState.WAIT_PAY), null, null, null, new EventTrigger<>(new Event<>(OrderEvent.CREATE)), "下单");
        machine.setInitialTransition(initialTransition);


        machine.setEnd(new State<>(OrderState.COMPLETED));
        EnumSet<OrderState> orderStates = EnumSet.allOf(OrderState.class);
        machine.setStates(orderStates.stream().map(State::new).toList());

        EnumSet<OrderEvent> orderEvents = EnumSet.allOf(OrderEvent.class);
        machine.setEvents(orderEvents.stream().map(Event::new).toList());

        List<Transition<OrderState, OrderEvent, Object>> transitions = Lists.newArrayList();
        DefaultExternalTransition<OrderState, OrderEvent, Object> externalTransition
                = new DefaultExternalTransition<>(new State<>(OrderState.WAIT_PAY), new State<>(OrderState.WAIT_DELIVER), null, null, null, new EventTrigger<>(new Event<>(OrderEvent.PAY)), "");
        transitions.add(externalTransition);
        machine.setTransitions(transitions);

        machine.setStateMachinePersist(new InMemoryStateMachinePersist<>());

        machine.setStateMachineLock(new DefaultStateMachineLock<>());


        machine.init("order001", OrderEvent.CREATE, null);


        machine.sendEvent("order001", OrderEvent.PAY, null);

    }
}
