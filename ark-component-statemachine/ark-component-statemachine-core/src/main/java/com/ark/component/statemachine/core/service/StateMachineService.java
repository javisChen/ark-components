// package com.ark.component.statemachine.core.service;
//
// import com.alibaba.fastjson2.JSON;
// import com.ark.component.statemachine.core.*;
// import com.ark.component.statemachine.core.lock.DefaultStateMachineLock;
// import com.ark.component.statemachine.core.lock.StateMachineLock;
// import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
// import com.ark.component.statemachine.core.persist.StateMachinePersist;
// import com.ark.component.statemachine.core.transition.Transition;
// import com.google.common.collect.Maps;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.transaction.support.TransactionSynchronization;
// import org.springframework.transaction.support.TransactionSynchronizationManager;
// import org.springframework.util.Assert;
//
// import java.util.List;
//
// @Slf4j
// public class StateMachineService<S, E> implements IStateMachineService<S, E> {
//
//     private final StateMachine<S, E> stateMachine;
//
//     private final Transition<S, E> initialTransition;
//
//     private StateMachinePersist<S, E> stateMachinePersist = new InMemoryStateMachinePersist<>();
//
//     private StateMachineLock<S> stateMachineLock = new DefaultStateMachineLock<>();
//
//     public StateMachineService(String machineId,
//                                List<State<S>> states,
//                                State<S> initial,
//                                State<S> end,
//                                List<Event<E>> events,
//                                Transition<S, E> initialTransition,
//                                List<Transition<S, E>> transitions,
//                                StateMachine<S, E> stateMachine,
//                                StateMachinePersist<S, E> stateMachinePersist,
//                                StateMachineLock<S> stateMachineLock) {
//         this.stateMachine = stateMachine;
//         Assert.hasText(machineId, "id must not be blank");
//         Assert.notNull(initial, "initial must not be null");
//         Assert.notNull(end, "end must not be null");
//         Assert.notEmpty(states, "state must not be empty");
//         Assert.notEmpty(events, "events must not be empty");
//         Assert.notEmpty(transitions, "transitions must not be empty");
//         this.initialTransition = initialTransition;
//         if (stateMachinePersist != null) {
//             this.stateMachinePersist = stateMachinePersist;
//         }
//         if (stateMachineLock != null) {
//             this.stateMachineLock = stateMachineLock;
//         }
//     }
//
//     @Override
//     public <P> void fireEvent(String bizId, E event, P params) {
//         Assert.hasText(bizId, "bizId must not be null");
//         Assert.notNull(event, "event must not be null");
//
//         // 取出业务数据
//         StateData stateData = stateMachinePersist.read(stateMachine.getMachineId(), bizId);
//
//         if (stateData == null) {
//             throw new StateMachineException("State object has not been initialized");
//         }
//
//         if (stateData.getState().equals(stateMachine.getEnd().toString())) {
//             throw new StateMachineException("State object has been ended");
//         }
//
//         // 锁定当前状态数据
//         if (!stateMachineLock.lock(stateData)) {
//             throw new StateMachineException("State object is being processed");
//         }
//
//         try {
//
//             String currentState = stateData.getState();
//
//             S s = stateMachine.nextState(currentState, event, params);
//
//
//             Transition<S, E> transition = stateMachine.findTransition(new Event<>(event), currentState);
//             if (transition == null) {
//                 throw new StateMachineException("Not found transition: source=[%s], event=[%s]".formatted(currentState, event));
//             }
//
//             StateContext<S, E> stateContext = stateMachine.buildContext(stateData.getBizId(), params, transition);
//
//             if (!transition.executeGuards(stateContext)) {
//                 return;
//             }
//
//             transition.executeActions(stateContext);
//
//             // do persist
//             stateMachinePersist.write(buildStateData(stateData.getId(), stateContext), stateContext);
//
//         } finally {
//             unlock(stateData);
//         }
//
//     }
//
//     private void unlock(StateData stateData) {
//         if (TransactionSynchronizationManager.isSynchronizationActive()) {
//             TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//                 @Override
//                 public void afterCommit() {
//                     stateMachineLock.unlock(stateData);
//                 }
//             });
//         } else {
//             stateMachineLock.unlock(stateData);
//         }
//     }
//
//     private StateData buildStateData(Long stateDataId, StateContext<S, E> stateContext) {
//         boolean ended = stateContext.getTransition().getTarget().toString().equals(end.toString());
//         StateData data = new StateData();
//         data.setId(stateDataId);
//         data.setMachineId(stateContext.getMachineId());
//         data.setBizId(stateContext.getBizId());
//         data.setState(stateContext.getTransition().getTarget().toString());
//         data.setEnded(ended);
//         data.setExtras(Maps.newHashMap());
//         return data;
//     }
//
//     public <P> void init(String bizId, E event, P params) {
//
//         // 取出业务数据
//         StateData stateData = stateMachinePersist.read(this.machineId, bizId);
//
//         Assert.isNull(stateData, "State object has been initialized");
//
//         StateContext<S, E> ctx = buildContext(bizId, params, this.initialTransition);
//
//         if (!initialTransition.executeGuards(ctx)) {
//             return;
//         }
//
//         initialTransition.executeActions(ctx);
//
//         stateMachinePersist.write(buildStateData(null, ctx), ctx);
//
//     }
//
//     @Override
//     public String toString() {
//         return JSON.toJSONString(this);
//     }
// }
