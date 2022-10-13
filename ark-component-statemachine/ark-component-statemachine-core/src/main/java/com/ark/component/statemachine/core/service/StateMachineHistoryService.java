package com.ark.component.statemachine.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ark.component.statemachine.dao.entity.StateMachineHistoryDO;
import com.ark.component.statemachine.dao.mapper.StateMachineHistoryMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 状态机历史表 Service
 * </p>
 *
 * @author DOP
 * @since 2022-04-04
 */
@Service
public class StateMachineHistoryService extends ServiceImpl<StateMachineHistoryMapper, StateMachineHistoryDO>
        implements IService<StateMachineHistoryDO> {

}
