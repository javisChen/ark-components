package com.ark.component.statemachine.core.persist;

import cn.hutool.core.util.IdUtil;
import com.ark.component.statemachine.core.StateData;
import com.ark.component.statemachine.core.StateMachineException;
import com.ark.component.statemachine.core.persist.data.StateDO;
import com.google.common.collect.Maps;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class JdbcStateMachinePersist<S extends Enum<S>, E> implements StateMachinePersist {

    private final JdbcTemplate jdbcTemplate;

    private final static String INSERT_STATE_HISTORY_SQL = "insert into stm_history (id, machine_id, biz_id, event, pre_state, current_state, extras, remark, gmt_create, creator)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private final static String INSERT_STATE_SQL = "insert into stm_state (id, machine_id, biz_id, state, ended, creator, modifier)" +
            "values (?, ?, ?, ?, ?, ?, ?)";

    private final static String UPDATE_STATE_SQL = "update stm_state set state = ?, ended = ?, gmt_modified = ?, modifier = ? where id= ?";

    private final static String SELECT_STATE_SQL = "select * from stm_state where machine_id = ? and biz_id = ?";

    private final static String SELECT_STATE_ID_SQL = "select id from stm_state where machine_id = ? and biz_id = ?";

    public JdbcStateMachinePersist(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StateData read(String machineId, String bizId) {
        StateDO stateDO;
        try {
            stateDO = jdbcTemplate.queryForObject(SELECT_STATE_SQL, new DataClassRowMapper<>(StateDO.class), machineId, bizId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        if (stateDO == null) {
            return null;
        }
        StateData stateData = new StateData();
        stateData.setId(stateDO.getId());
        stateData.setState(stateDO.getState());
        stateData.setMachineId(stateDO.getMachineId());
        stateData.setBizId(stateDO.getBizId());
        stateData.setEnded(stateDO.getEnded() == 1);
        stateData.setExtras(Maps.newHashMap());
        return stateData;
    }

    @Override
    public void write(StateData stateData) {
        long operator = 0L;
        String state = stateData.getState();
        String machineId = stateData.getMachineId();
        String bizId = stateData.getBizId();
        boolean ended = stateData.isEnded();

        saveOrUpdateState(ended, state, operator, machineId, bizId);

        saveHistory(ended, state, operator, machineId, bizId);
    }

    private void saveHistory(boolean ended, String state, long operator, String machineId, String bizId) {
        jdbcTemplate.update(INSERT_STATE_HISTORY_SQL, IdUtil.getSnowflakeNextId(), machineId,
                bizId, state, ended, operator);
    }

    private void saveOrUpdateState(boolean ended, String state, long operator, String machineId, String bizId) {
        LocalDateTime now = LocalDateTime.now();
        Long stateId = jdbcTemplate.queryForObject(SELECT_STATE_ID_SQL, Long.class, machineId, bizId);
        if (stateId!= null) {
            if (jdbcTemplate.update(UPDATE_STATE_SQL, state, ended, now, operator, stateId) == 0) {
                throw new StateMachineException("State object has been changed on other");
            }
        } else {
            jdbcTemplate.update(INSERT_STATE_SQL, IdUtil.getSnowflakeNextId(), machineId,
                    bizId, state, ended, operator, operator);
        }
    }

}
