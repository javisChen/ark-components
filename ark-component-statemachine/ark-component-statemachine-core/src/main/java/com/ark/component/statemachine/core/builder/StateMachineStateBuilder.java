package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.transition.InitialTransition;

import java.util.Collection;
import java.util.List;

public class StateMachineStateBuilder<S, E> {

    private List<State<S>> states;

    private State<S> initial;

    private State<S> end;

    private InitialTransition<S, E> initialTransition;

    public List<State<S>> getStates() {
        return states;
    }

    public InitialTransition<S, E> getInitialTransition() {
        return initialTransition;
    }

    public State<S> getInitial() {
        return initial;
    }

    public State<S> getEnd() {
        return end;
    }

    public StateMachineStateBuilder<S, E> initial(S initial) {
        this.initial = new State<>(initial);
        this.initialTransition = new InitialTransition<>(this.initial, null, null, "");
        return this;
    }

    public StateMachineStateBuilder<S, E> initial(S initial, Collection<Action<S, E>> actions, Collection<Guard<S, E>> guards) {
        this.initial = new State<>(initial);
        this.initialTransition = new InitialTransition<>(this.initial, guards, actions, "");
        return this;
    }

    public StateMachineStateBuilder<S, E> end(S end) {
        this.end = new State<>(end);
        return this;
    }

    public StateMachineStateBuilder<S, E> states(Collection<S> states) {
        this.states = states.stream().map(State::new).toList();
        return this;
    }

}
