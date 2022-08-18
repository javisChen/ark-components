package com.kt.component.statemachine.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kt.component.statemachine.dao.entity.StateMachineDefinitionDO;
import com.kt.component.statemachine.dao.mapper.StateMachineDefinitionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 状态机规则定义表 Service
 * </p>
 *
 * @author DOP
 * @since 2022-04-04
 */
@Service
public class StateMachineDefinitionService extends ServiceImpl<StateMachineDefinitionMapper, StateMachineDefinitionDO>
        implements IService<StateMachineDefinitionDO> {

    public StateMachineDefinitionDO getByBizCode(String bizCode) {
        return lambdaQuery()
                .eq(StateMachineDefinitionDO::getBizCode, bizCode)
                .one();
    }
}
