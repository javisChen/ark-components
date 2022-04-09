package com.kt.component.statemachine.core.service;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.common.ParamsChecker;
import com.kt.component.statemachine.core.StateMachineDefinition;
import com.kt.component.statemachine.core.exception.StateMachineException;
import com.kt.component.statemachine.dao.entity.StateMachineDefinitionDO;
import com.kt.component.statemachine.dao.entity.StateMachineHistoryDO;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;
import org.springframework.stereotype.Component;

@Component
public class StateMachineService {

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
        ParamsChecker.throwIfIsBlank(bizCode, new StateMachineException("[bizCode] cannot be blank"));

        StateMachineDefinitionDO definitionDO = stateMachineDefinitionService.getByBizCode(bizCode);
        ParamsChecker.throwIfIsNull(definitionDO, new StateMachineException("[bizCode] is not define"));

        String config = definitionDO.getConfig();
        return JSONObject.parseObject(config, StateMachineDefinition.class);

    }

    public StateMachineRuntimeDO getRuntime(String bizCode, Long bizId) {
        return stateMachineRuntimeService.getByBizCodeAndBizId(bizCode, bizId);
    }

    public void saveRuntime(Long runtimeId, String bizCode, Long bizId, String state, boolean finished) {
        StateMachineRuntimeDO entity = new StateMachineRuntimeDO();
        if (runtimeId == null) {
            entity.setBizCode(bizCode);
            entity.setBizId(bizId);
            entity.setState(state);
            entity.setFinished(false);
            stateMachineRuntimeService.save(entity);
        } else {
            entity.setId(runtimeId);
            entity.setState(state);
            entity.setFinished(finished);
            stateMachineRuntimeService.updateById(entity);
        }
    }

    public void saveHistory(StateMachineHistoryDO stateMachineHistoryDO) {
        stateMachineHistoryService.save(stateMachineHistoryDO);
    }
}
