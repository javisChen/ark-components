package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateMachine;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.lock.StateMachineLock;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.InitialTransition;
import com.ark.component.statemachine.core.transition.Transition;

import java.util.ArrayList;
import java.util.Collection;

public class StateMachineBuilder<S, E> {

    private StateMachineBuilder() {

    }

    public static <S, E> StateMachineBuilder<S, E> builder() {
        return new StateMachineBuilder<>();
    }

    private String id;

    private Collection<State<S>> states;

    private State<S> initial;

    private State<S> end;

    protected Collection<Event<E>> events = new ArrayList<>();

    private InitialTransition<S, E> initialTransition;

    private Collection<Transition<S, E>> transitions;

    private StateMachinePersist<S> persist;

    private StateMachineLock<S> lock;

    private Collection<TransitionBuilder<S, E>> transitionBuilders = new ArrayList<>(10);

    public StateMachineBuilder<S, E> id(String id) {
        this.id = id;
        return this;
    }

    public StateMachineBuilder<S, E> initial(S initial) {
        this.initial = new State<>(initial);
        this.initialTransition = new InitialTransition<>(this.initial, null, null, "");
        return this;
    }

    public StateMachineBuilder<S, E> initial(S initial, Collection<Action<E>> actions, Collection<Guard<E>> guards) {
        this.initial = new State<>(initial);
        this.initialTransition = new InitialTransition<>(this.initial, guards, actions, "");
        return this;
    }

    public StateMachineBuilder<S, E> end(S end) {
        this.end = new State<>(end);
        return this;
    }

    public StateMachineBuilder<S, E> states(Collection<S> states) {
        this.states = states.stream().map(State::new).toList();
        return this;
    }


    public StateMachineBuilder<S, E> persist(StateMachinePersist<S> persist) {
        this.persist = persist;
        return this;
    }

    public StateMachineBuilder<S, E> lock(StateMachineLock<S> lock) {
        this.lock = lock;
        return this;
    }

    public TransitionBuilder<S, E> withTransition() {
        TransitionBuilder<S, E> transitionBuilder = new TransitionBuilder<>(this);
        transitionBuilders.add(transitionBuilder);
        return transitionBuilder;
    }

    public StateMachine<S, E> build() {

        transitions = transitionBuilders.stream().map(TransitionBuilder::build).toList();

        return new StateMachine<>(id,
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
