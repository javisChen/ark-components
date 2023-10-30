package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.Event;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.action.Action;
import com.ark.component.statemachine.core.guard.Guard;
import com.ark.component.statemachine.core.transition.ExternalTransition;
import com.ark.component.statemachine.core.transition.Transition;
import com.ark.component.statemachine.core.trigger.EventTrigger;
import com.ark.component.statemachine.core.trigger.Trigger;

import java.util.ArrayList;
import java.util.Collection;

public class ExternalTransitionBuilder<S, E> {

    private State<S> target;
    private Collection<Event<E>> events = new ArrayList<>();

    private Collection<Action<S, E>> actions;
    private State<S> source;
    private Collection<Guard<S, E>> guards;
    private Trigger<E> trigger;
    private String name;

    private final StateMachineTransitionBuilder<S, E> builder;

    public ExternalTransitionBuilder(StateMachineTransitionBuilder<S, E> builder) {
        this.builder = builder;
    }

    public ExternalTransitionBuilder<S, E> name(String name) {
        this.name = name;
        return this;
    }

    public ExternalTransitionBuilder<S, E> source(S source) {
        this.source = new State<>(source);
        return this;
    }

    public ExternalTransitionBuilder<S, E> target(S target) {
        this.target = new State<>(target);
        return this;
    }

    public ExternalTransitionBuilder<S, E> event(E event) {
        Event<E> e = new Event<>(event);
        this.trigger = new EventTrigger<>(e);
        if (!this.events.contains(e)) {
            this.events.add(e);
        }
        this.events.add(new Event<>(event));
        return this;
    }

    public ExternalTransitionBuilder<S, E> guards(Collection<Guard<S, E>> guards) {
        this.guards = guards;
        return this;
    }

    public ExternalTransitionBuilder<S, E> actions(Collection<Action<S, E>> actions) {
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

    public Collection<Event<E>> getEvents() {
        return events;
    }

    public StateMachineTransitionBuilder<S, E> and() {
        return builder;
    }
}
