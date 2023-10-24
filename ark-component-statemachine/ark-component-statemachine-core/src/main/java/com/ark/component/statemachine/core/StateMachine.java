package com.ark.component.statemachine.core;

import com.ark.component.statemachine.core.lock.StateMachineLock;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import com.ark.component.statemachine.core.transition.Transition;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Data
@Slf4j
public class StateMachine<S, E, T> {

    private String id;

    private List<State<S>> states;

    private State<S> initial;

    private State<S> end;

    private List<Event<E>> events;

    private Transition<S, E, T> initialTransition;

    private List<Transition<S, E, T>> transitions;

    private StateMachinePersist<S, E, T> stateMachinePersist;

    private StateMachineLock<S> stateMachineLock;

    public <P> void sendEvent(String id, E event, P params) {

        // 取出业务数据
        StateData<S> stateData = stateMachinePersist.read(id);

        if (stateData == null) {
            throw new StateMachineException("State object has not been initialized");
        }

        if (stateData.getState().equals(end)) {
            throw new StateMachineException("State object has been ended");
        }

        // 锁定当前状态数据
        if (!stateMachineLock.lock(stateData)) {
            throw new StateMachineException("State object is being processed");
        }

        try {
            State<S> currentState = stateData.getState();

            Transition<S, E, T> transition = findTransition(new Event<>(event), currentState);
            if (transition == null) {
                throw new StateMachineException(String.format("Cannot find transition, Event = %s, State = %s", event, currentState));
            }

            StateContext<E, P> stateContext = buildContext(stateData, params, event);

            if (!transition.executeGuards(stateContext)) {
                return;
            }

            transition.executeActions(stateContext);

            State<S> target = transition.getTarget();
            stateData.setState(target);

            // write
            stateMachinePersist.write(stateData);

        } finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    stateMachineLock.unlock(stateData);
                }
            });
        }

    }

    public <P> void init(String id, E event, P params) {

        // 取出业务数据
        StateData<S> stateData = stateMachinePersist.read(id);

        if (stateData != null) {
            throw new StateMachineException("State object has been initialized");
        }

        stateData = new StateData<>();
        stateData.setMachineId(this.id);
        stateData.setId(id);
        stateData.setState(initial);
        stateData.setVariables(Maps.newHashMap());
        StateContext<E, P> ctx = buildContext(stateData, params, event);

        if (!initialTransition.executeGuards(ctx)) {
            return;
        }

        initialTransition.executeActions(ctx);

        stateMachinePersist.write(stateData);

    }

    private <P> StateContext<E, P> buildContext(StateData<S> stateData, P params, E event) {
        StateContext<E, P> stateContext = new StateContext<>();
        stateContext.setBizCode(stateData.getMachineId());
        stateContext.setBizId(stateData.getId());
        stateContext.setEvent(event);
        stateContext.setParams(params);
        stateContext.setExtras(Maps.newHashMap());
        return stateContext;
    }

    private Transition<S, E, T>  findTransition(Event<E> event, State<S> currentState) {
        if (transitions == null || transitions.size() == 0) {
            return null;
        }
        for (Transition<S, E, T> transition : transitions) {
            if (currentState.getId().equals(transition.getSource().getId())
                    && transition.getTrigger().getEvent().getId().equals(event.getId())) {
                return transition;
            }
        }
        return null;
    }
}
