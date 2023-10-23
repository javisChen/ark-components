package com.ark.component.statemachine.core;

import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.Transition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class StateMachine<S, E, T> {

    private String id;

    private List<State<S>> states;

    private List<Event<E>> events;

    private List<Transition<S, E, T>> transitions;

    private StateMachinePersist<S, E, T> stateMachinePersist;

    public void sendEvent(String id, E event, Object context) {
        // 取出业务数据
        StateMachineContext<S, E, T> machineContext = stateMachinePersist.read(id);

        if (machineContext == null) {
            throw new StateMachineException("State object has not been initialized");
        }

        S currentState = machineContext.getState();

        Transition<S, E, T> transition = findTransition(event, currentState);
        if (transition == null) {
            throw new StateMachineException(String.format("Cannot find transition, Event = %s, State = %s", event, currentState));
        }

        if (!transition.transit(machineContext)) {
            return;
        }

        try {
            transition.executeTransitionActions(machineContext);
        } catch (Exception e) {
            // aborting, executor should stop possible loop checking possible transitions
            // causing infinite execution
            log.warn("Aborting as transition " + transition, e);
            throw new StateMachineException("Aborting as transition " + transition + " caused error ", e);
        }


    }

    private Transition<S, E, T> findTransition(E event, S currentState) {
        if (transitions == null || transitions.size() == 0) {
            return null;
        }
        for (Transition<S, E, T> transition : transitions) {
            if (currentState.equals(transition.getSource())
                    && transition.getTrigger().getEvent().equals(event)) {
                return transition;
            }
        }
        return null;
    }
}
