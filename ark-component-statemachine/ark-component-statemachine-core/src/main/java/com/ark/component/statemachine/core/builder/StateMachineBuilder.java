package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateMachine;
import com.ark.component.statemachine.core.lock.StateMachineLock;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.InitialTransition;
import com.ark.component.statemachine.core.transition.Transition;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class StateMachineBuilder<S, E> implements Builder<S, E> {

    private final StateMachineTransitionBuilder<S, E> transitionBuilder = new StateMachineTransitionBuilder<>();
    private final StateMachineConfigurationBuilder<S, E> configurationBuilder = new StateMachineConfigurationBuilder<>();
    private final StateMachineStateBuilder<S, E> stateBuilder = new StateMachineStateBuilder<>();
    public static <S, E> StateMachineBuilder<S, E> newBuilder() {
        return new StateMachineBuilder<S, E>();
    }

    public StateMachineBuilder<S, E> withConfiguration(Consumer<StateMachineConfigurationBuilder<S, E>> consumer) {
        consumer.accept(configurationBuilder);
        return this;
    }

    public StateMachineBuilder<S, E> withStates(Consumer<StateMachineStateBuilder<S, E>> consumer) {
        consumer.accept(stateBuilder);
        return this;
    }
    public StateMachineBuilder<S, E> withTransition(Consumer<StateMachineTransitionBuilder<S, E>> consumer) {
        consumer.accept(transitionBuilder);
        return this;
    }

    public StateMachine<S, E> build() {

        String machineId = configurationBuilder.getMachineId();
        StateMachineLock<S> lock = configurationBuilder.getLock();
        StateMachinePersist<S, E> persist = configurationBuilder.getPersist();

        Collection<State<S>> states = stateBuilder.getStates();
        State<S> initial = stateBuilder.getInitial();
        InitialTransition<S, E> initialTransition = stateBuilder.getInitialTransition();
        State<S> end = stateBuilder.getEnd();

        List<Transition<S, E>> transitions = transitionBuilder.build();
        List<Event<E>> events = transitions.stream().map(e -> e.getTrigger().getEvent()).toList();

        return new StateMachine<>(machineId,
                states,
                initial,
                end,
                events,
                initialTransition,
                transitions,
                persist,
                lock);
    }

}
