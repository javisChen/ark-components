package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.transition.ExternalTransition;
import com.ark.component.statemachine.core.transition.Transition;
import com.ark.component.statemachine.core.transition.TransitionKind;
import com.ark.component.statemachine.core.trigger.EventTrigger;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.Collection;

public class TransitionBuilder<S, E> implements Builder<S, E> {

    private State<S> target;
    private Collection<Action<E>> actions;
    private State<S> source;
    private TransitionKind kind;
    private Collection<Guard<E>> guards;
    private Trigger<S, E> trigger;
    private String name;

    private final StateMachineBuilder<S, E> parent;

    public TransitionBuilder(StateMachineBuilder<S, E> machineBuilder) {
        this.parent = machineBuilder;
    }

    public TransitionBuilder<S, E> source(S source) {
        this.source = new State<>(source);
        return this;
    }

    public TransitionBuilder<S, E> target(S target) {
        this.target = new State<>(target);
        return this;
    }
    public TransitionBuilder<S, E> event(E event) {
        Event<E> e = new Event<>(event);
        if (!this.parent.events.contains(e)) {
            this.parent.events.add(e);
        }
        this.trigger = new EventTrigger<>(e);
        return this;
    }

    public TransitionBuilder<S, E> guards(Collection<Guard<E>> guards) {
        this.guards = guards;
        return this;
    }

    public TransitionBuilder<S, E> actions(Collection<Action<E>> actions) {
        this.actions = actions;
        return this;
    }

    public Transition<S, E> build() {
        return new ExternalTransition<>(this.source,
                this.target,
                this.guards,
                this.actions,
                this.trigger,
                this.name);
    }

    public StateMachineBuilder<S, E> and() {
        return parent;
    }

}

