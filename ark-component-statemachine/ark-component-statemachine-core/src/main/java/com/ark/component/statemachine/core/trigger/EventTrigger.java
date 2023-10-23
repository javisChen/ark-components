package com.ark.component.statemachine.core.trigger;

public class EventTrigger<S, E> implements Trigger<S, E> {

    private final E event;

    public EventTrigger(E event) {
        this.event = event;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

}
