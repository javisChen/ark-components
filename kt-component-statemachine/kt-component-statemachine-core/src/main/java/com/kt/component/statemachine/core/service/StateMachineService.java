package com.kt.component.statemachine.core.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kt.component.exception.BizException;
import com.kt.component.orm.mybatis.base.BaseEntity;
import com.kt.component.statemachine.core.StateMachineDefinition;
import com.kt.component.statemachine.core.exception.StateMachineException;
import com.kt.component.statemachine.dao.entity.StateMachineDefinitionDO;
import com.kt.component.statemachine.dao.entity.StateMachineHistoryDO;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class StateMachineService {

    private ConcurrentHashMap<String, StateMachineDefinition> cache = new ConcurrentHashMap<>(16);
    private final StateMachineHistoryService stateMachineHistoryService;
    private final StateMachineRuntimeService stateMachineRuntimeService;
    private final StateMachineDefinitionService stateMachineDefinitionService;

    public StateMachineService(StateMachineHistoryService stateMachineHistoryService,
                               StateMachineRuntimeService stateMachineRuntimeService,
                               StateMachineDefinitionService stateMachineDefinitionService) {
        this.stateMachineHistoryService = stateMachineHistoryService;
        this.stateMachineRuntimeService = stateMachineRuntimeService;
        this.stateMachineDefinitionService = stateMachineDefinitionService;
    }

    public StateMachineDefinition getDefinition(String bizCode) {
        StateMachineDefinition definition = cache.get(bizCode);
        if (Objects.nonNull(definition)) {
            if (log.isDebugEnabled()) {
                log.debug("从缓存读取 [{}] 的状态机配置 = {}", bizCode, definition);
            }
            return definition;
        }
        if (StringUtils.isBlank(bizCode)) {
            throw new StateMachineException("[bizCode] cannot be blank");
        }
        StateMachineDefinitionDO definitionDO = stateMachineDefinitionService.getByBizCode(bizCode);
        if (Objects.isNull(definitionDO) || definitionDO.getConfig() == null) {
            throw new StateMachineException("bizCode -> [" + bizCode + "] is not define");
        }
        String config = definitionDO.getConfig();
        definition = JSONObject.parseObject(config, StateMachineDefinition.class);
        if (definition == null) {
            throw new StateMachineException("definition config is empty");
        }
        cache.put(bizCode, definition);
        return definition;

    }

    public StateMachineRuntimeDO getRuntime(String bizCode, Long bizId) {
        return stateMachineRuntimeService.getByBizCodeAndBizId(bizCode, bizId);
    }

    public void savDOrUpdateRuntime(StateMachineRuntimeDO runtime, String bizCode, Long bizId, String state, boolean finished) {
        StateMachineRuntimeDO entity = new StateMachineRuntimeDO();
        if (runtime == null) {
            entity.setBizCode(bizCode);
            entity.setBizId(bizId);
            entity.setState(state);
            entity.setFinished(false);
            stateMachineRuntimeService.save(entity);
        } else {
            LambdaUpdateWrapper<StateMachineRuntimeDO> qw = new LambdaUpdateWrapper<>();
            // 更新带上当前状态，防止并发访问
            qw.eq(StateMachineRuntimeDO::getBizId, bizId)
                    .eq(StateMachineRuntimeDO::getState, runtime.getState())
                    .set(StateMachineRuntimeDO::getState, state)
                    .set(StateMachineRuntimeDO::getFinished, finished);
            boolean result = stateMachineRuntimeService.update(qw);
            if (!result) {
                throw new BizException("状态已被其他操作修改，请刷新重试");
            }
        }
    }

    public void saveHistory(StateMachineHistoryDO stateMachineHistoryDO) {
        stateMachineHistoryService.save(stateMachineHistoryDO);
    }

    public List<StateMachineHistoryDO> getLastTowHistories(String bizCode, Long bizId) {
        return stateMachineHistoryService.lambdaQuery()
                .eq(StateMachineHistoryDO::getBizCode, bizCode)
                .eq(StateMachineHistoryDO::getBizId, bizId)
                .orderByDesc(BaseEntity::getGmtCreate)
                .last("limit 2")
                .list();
    }

    public void updateRuntime(String bizCode, Long bizId, String state) {
        stateMachineRuntimeService.updateStateByBizIdAndBizCode(bizId, bizCode, state, false);
    }
}
