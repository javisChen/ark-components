package com.ark.component.statemachine.core;
public class State<S> {
    private final S value;
    public State (S value) {
        this.value = value;
    }

    public S getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
