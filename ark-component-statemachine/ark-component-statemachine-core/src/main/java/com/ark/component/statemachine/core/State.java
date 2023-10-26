package com.ark.component.statemachine.core;
public class State<S> {
    private final S id;
    public State (S id) {
        this.id = id;
    }

    public S getId() {
        return id;
    }
}
