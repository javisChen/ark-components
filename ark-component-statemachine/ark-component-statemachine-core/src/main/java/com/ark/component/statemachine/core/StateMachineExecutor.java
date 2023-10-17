package com.ark.component.statemachine.core;

import cn.hutool.core.util.ArrayUtil;
import com.ark.component.statemachine.core.action.ActionExecutor;
import com.ark.component.statemachine.core.exception.AlreadyFinishedException;
import com.ark.component.statemachine.core.exception.StateMachineException;
import com.ark.component.statemachine.core.exception.TransitionNotFoundException;
import com.ark.component.statemachine.core.guard.GuardExecutor;
import com.ark.component.statemachine.core.service.StateMachineService;
import com.ark.component.statemachine.dao.entity.StateMachineHistoryDO;
import com.ark.component.statemachine.dao.entity.StateMachineRuntimeDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 状态机
 *
 * @author jc
 */
@Slf4j
@Component
public class StateMachineExecutor {

    private final String initEvent = "INIT";
    private final String rollbackEvent = "ROLLBACK";
    private final StateMachineService stateMachineService;
    private final GuardExecutor guardExecutor;
    private final ActionExecutor actionExecutor;

    public StateMachineExecutor(StateMachineService stateMachineService,
                                GuardExecutor guardExecutor,
                                ActionExecutor actionExecutor) {
        this.stateMachineService = stateMachineService;
        this.guardExecutor = guardExecutor;
        this.actionExecutor = actionExecutor;
    }

    /**
     * 回滚状态机到上一个状态
     *
     * @param bizCode 业务编码
     * @param bizId   业务id
     * @param targetState   指定回滚到的状态
     */
    @Transactional(rollbackFor = Throwable.class)
    public StateMachineResult rollback(String bizCode, Long bizId, String targetState) {
        // 从状态机历史找出倒数第二条流转状态
        List<StateMachineHistoryDO> histories = stateMachineService.getLastTowHistories(bizCode, bizId);
        if (CollectionUtils.isEmpty(histories)) {
            return StateMachineResult.withFail("Can't not found state history").build();
        }
        if (histories.size() <= 1) {
            return StateMachineResult.withFail("Init state can't not be rollback").build();
        }
        StateMachineHistoryDO lastTowHistory = histories.get(1);
        StateMachineHistoryDO lastOneHistory = histories.get(0);
        if (StringUtils.isBlank(targetState)) {
            targetState = lastTowHistory.getCurrentState();
        }
        stateMachineService.updateRuntime(bizCode, bizId, targetState);

        StateMachineHistoryDO historyDO = new StateMachineHistoryDO();
        historyDO.setBizCode(lastTowHistory.getBizCode());
        historyDO.setBizId(lastTowHistory.getBizId());
        historyDO.setEvent(rollbackEvent);
        historyDO.setPreState(lastOneHistory.getCurrentState());
        historyDO.setCurrentState(targetState);
        stateMachineService.saveHistory(historyDO);

        return StateMachineResult.withSuccess()
                .withEvent(rollbackEvent)
                .withBizCode(bizCode)
                .withBizId(bizId)
                .withState(historyDO.getCurrentState()).build();
    }


    /**
     * 初始化状态机
     *
     * @param bizCode 业务编码
     * @param params  参数
     * @return 成功=true，失败=false
     */
    @Transactional(rollbackFor = Exception.class)
    public <P> StateMachineResult init(String bizCode, P params) throws StateMachineException {

        // 根据bizCode查出状态机定义规则
        StateMachineDefinition stateMachineDefinition = stateMachineService.getDefinition(bizCode);

        // 检查事件是否存在
        checkEventIsLegal(initEvent, stateMachineDefinition);

        // 组装上下文
        StateMachineContext context = assembleContext(bizCode, initEvent, params);

        // 执行状态转换
        return executeTransition(null, stateMachineDefinition, context);
    }


    /**
     * 执行状态机
     *
     * @param bizCode 业务编码
     * @param bizId   业务ID
     * @param params  参数
     * @return 成功=true，失败=false
     */
    @Transactional(rollbackFor = Throwable.class)
    public <P> StateMachineResult execute(String bizCode, Long bizId, String event, P params)
            throws AlreadyFinishedException, TransitionNotFoundException {

        // 根据bizCode查出状态机定义规则
        StateMachineDefinition stateMachineDefinition = stateMachineService.getDefinition(bizCode);

        // 检查事件是否存在
        checkEventIsLegal(event, stateMachineDefinition);

        // 组装上下文
        StateMachineContext context = assembleContext(bizId, bizCode, event, params);

        // 根据bizCode和bizId查出对应状态机运行状态
        StateMachineRuntimeDO runtime = getRuntime(context);

        // 执行状态转换
        return executeTransition(runtime, stateMachineDefinition, context);
    }

    private StateMachineResult executeTransition(StateMachineRuntimeDO runtime,
                                                 StateMachineDefinition definition,
                                                 StateMachineContext context)
            throws AlreadyFinishedException, TransitionNotFoundException {
        List<StateMachineDefinition.Transitions> transitions = definition.getTransitions();
        String event = context.getEvent();
        // 状态机必须先进行初始化
        if (Objects.isNull(runtime)) {
            if (initEvent.equals(event)) {
                // 默认第一个transition是初始化事件
                return doExecuteTransition(context, transitions.get(0), definition, null);
            }
            throw new StateMachineException("please drive [init] event first");
        }

        // 禁止重复初始化
        if (initEvent.equals(event)) {
            throw new StateMachineException("state has been initialized");
        }

        // 如果规则有定义finalState的话，当状态机已驱动到最终状态后，不得再继续运行
        if (hasFinalState(definition) && runtime.getFinished()) {
            throw new AlreadyFinishedException("state already finished");
        }

        // 从第二个transition对象开始遍历
        for (int i = 1, transitionsSize = transitions.size(); i < transitionsSize; i++) {
            StateMachineDefinition.Transitions transition = transitions.get(i);
            // 根据source和event确定需要执行的transition
            if (transition.getEvent().equals(event) && transition.getSource().contains(runtime.getState())) {
                return doExecuteTransition(context, transition, definition, runtime);
            }
        }
        throw new TransitionNotFoundException(String.format("cannot find match transition for state:[%s] and event:[%s]",
                runtime.getState(), event));
    }

    /**
     * 是否有配置最终状态
     */
    private boolean hasFinalState(StateMachineDefinition definition) {
        return ArrayUtil.isNotEmpty(definition.getFinalState());
    }

    private StateMachineResult doExecuteTransition(StateMachineContext context,
                                                   StateMachineDefinition.Transitions transition,
                                                   StateMachineDefinition definition,
                                                   StateMachineRuntimeDO runtimeDO) {

        guardExecutor.execute(transition.getGuards(), context);

        actionExecutor.execute(transition.getActions(), context);

        if (context.getBizId() == null) {
            throw new StateMachineException("bizId cannot be null");
        }
        String state;
        if (Objects.isNull(runtimeDO)) {
            // 如果是首次驱动状态机，约定取第一个State作为初始化状态
            state = definition.getStates().get(0);
        } else {
            state = transition.getTarget();
        }
        saveStateMachineInfo(context, transition, definition, runtimeDO, state);
        return StateMachineResult
                .withSuccess()
                .withBizCode(context.getBizCode())
                .withBizId(context.getBizId())
                .withState(state)
                .withEvent(context.getEvent())
                .build();
    }

    private void saveStateMachineInfo(StateMachineContext context,
                                      StateMachineDefinition.Transitions transition,
                                      StateMachineDefinition definition,
                                      StateMachineRuntimeDO runtimeDO,
                                      String state) {
        String bizCode = context.getBizCode();
        Long bizId = context.getBizId();
        String event = context.getEvent();
        boolean finalEvent = isFinalEvent(transition, definition);
        stateMachineService.savDOrUpdateRuntime(runtimeDO, bizCode, bizId, state, finalEvent);

        StateMachineHistoryDO stateMachineHistoryDO = new StateMachineHistoryDO();
        stateMachineHistoryDO.setBizCode(bizCode);
        stateMachineHistoryDO.setBizId(bizId);
        stateMachineHistoryDO.setEvent(event);
        stateMachineHistoryDO.setPreState(runtimeDO == null ? "" : runtimeDO.getState());
        stateMachineHistoryDO.setCurrentState(transition.getTarget());
        stateMachineService.saveHistory(stateMachineHistoryDO);
    }

    private boolean isFinalEvent(StateMachineDefinition.Transitions transition, StateMachineDefinition definition) {
        return definition.getFinalState().contains(transition.getTarget());
    }

    private StateMachineContext assembleContext(Long bizId, String bizCode, String event, Object params) {
        return new StateMachineContext(bizCode, bizId, event, params, new HashMap<>(16));
    }

    private StateMachineContext assembleContext(String bizCode, String event, Object params) {
        return new StateMachineContext(bizCode, event, params, new HashMap<>(16));
    }

    private StateMachineRuntimeDO getRuntime(StateMachineContext context) {
        return stateMachineService.getRuntime(context.getBizCode(), context.getBizId());
    }

    private void checkEventIsLegal(String event, StateMachineDefinition stateMachineDefinition) {
        if (!stateMachineDefinition.getEvents().contains(event)) {
            throw new StateMachineException("event is illegal, please check the definition");
        }
    }
}
