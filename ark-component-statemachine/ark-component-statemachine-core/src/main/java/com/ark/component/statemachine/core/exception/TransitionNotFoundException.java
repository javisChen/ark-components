package com.ark.component.statemachine.core.exception;

/**
 * 状态机找不到状态流转/事件异常
 */
public class TransitionNotFoundException extends StateMachineException {

    public TransitionNotFoundException(Throwable cause) {
        super(cause);
    }

    public TransitionNotFoundException(String message) {
        super(message);
    }
}
