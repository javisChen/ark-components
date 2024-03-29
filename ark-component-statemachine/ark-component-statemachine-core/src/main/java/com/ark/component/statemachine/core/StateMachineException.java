package com.ark.component.statemachine.core;

public class StateMachineException extends RuntimeException {

    private final String message;

    public StateMachineException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
