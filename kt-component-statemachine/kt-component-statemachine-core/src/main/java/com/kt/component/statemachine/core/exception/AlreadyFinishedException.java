package com.kt.component.statemachine.core.exception;

/**
 * 状态机已流转到最终状态
 */
public class AlreadyFinishedException extends StateMachineException {

    public AlreadyFinishedException(Throwable cause) {
        super(cause);
    }

    public AlreadyFinishedException(String message) {
        super(message);
    }
}
