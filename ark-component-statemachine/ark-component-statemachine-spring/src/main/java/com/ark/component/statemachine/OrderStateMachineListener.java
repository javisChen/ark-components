package com.ark.component.statemachine;

import com.ark.component.statemachine.config.Events;
import com.ark.component.statemachine.config.OrderStates;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineListener implements StateMachineListener<OrderStates, Events> {

    @Override
    public void stateChanged(State<OrderStates, Events> from, State<OrderStates, Events> to) {
        System.out.println("from = " + from);
        System.out.println("to = " + to);
    }

    @Override
    public void stateEntered(State<OrderStates, Events> state) {

    }

    @Override
    public void stateExited(State<OrderStates, Events> state) {

    }

    @Override
    public void eventNotAccepted(Message<Events> event) {

    }

    @Override
    public void transition(Transition<OrderStates, Events> transition) {
    }

    @Override
    public void transitionStarted(Transition<OrderStates, Events> transition) {
    }

    @Override
    public void transitionEnded(Transition<OrderStates, Events> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<OrderStates, Events> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<OrderStates, Events> stateMachine) {

    }

    @Override
    public void stateMachineError(StateMachine<OrderStates, Events> stateMachine, Exception exception) {

    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<OrderStates, Events> stateContext) {
    }
}
