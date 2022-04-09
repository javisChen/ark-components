package com.kt.component.statemachine.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;
import com.kt.component.statemachine.dao.mapper.StateMachineRuntimeMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 状态机运行时表 Service
 * </p>
 *
 * @author EOP
 * @since 2022-04-04
 */

@Service
public class StateMachineRuntimeService extends ServiceImpl<StateMachineRuntimeMapper, StateMachineRuntimeDO>
        implements IService<StateMachineRuntimeDO> {

    public StateMachineRuntimeDO getByBizCodeAndBizId(String bizCode, Long bizId) {
        return lambdaQuery()
                .eq(StateMachineRuntimeDO::getBizCode, bizCode)
                .eq(StateMachineRuntimeDO::getBizId, bizId)
                .one();
    }

    public void updateStateByBizIdAndBizCode(Long bizId, String bizCode, String state) {
        lambdaUpdate()
                .set(StateMachineRuntimeDO::getState, state)
                .eq(StateMachineRuntimeDO::getBizId, bizId)
                .eq(StateMachineRuntimeDO::getBizCode, bizCode);
    }
}
