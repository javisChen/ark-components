package com.ark.component.statemachine.core.persist;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.TypeUtil;
import com.ark.component.statemachine.core.State;
import com.ark.component.statemachine.core.StateData;
import com.ark.component.statemachine.core.StateMachineException;
import com.ark.component.statemachine.core.persist.data.StateDO;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class JdbcStateMachinePersist<S extends Enum<S>> implements StateMachinePersist<S> {

    private Class<S> s;

    private Type type;

    private final JdbcTemplate jdbcTemplate;

    private final static String INSERT_STATE_SQL = "insert into stm_state (id, machine_id, biz_id, state, ended, creator, modifier)" +
            "values (?, ?, ?, ?, ?, ?, ?)";

    private final static String UPDATE_STATE_SQL = "update stm_state set state = ?, gmt_modified = ?, modifier = ? where id= ?";

    private final static String SELECT_SQL = "select * from stm_state where machine_id = ? and biz_id = ?";

    public JdbcStateMachinePersist(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        type = TypeUtil.getTypeArgument(new TypeToken<JdbcStateMachinePersist<S>>() {}.getType());
        System.out.println(type);
    }

    @Override
    public void write(StateData<S> stateData) {
        LocalDateTime now = LocalDateTime.now();
        String state = stateData.getState().getId().toString();
        long operator = 0L;
        if (stateData.getId() != null) {
            int updated = jdbcTemplate.update(UPDATE_STATE_SQL, state, now, operator, stateData.getId());
            if (updated == 0) {
                throw new StateMachineException("State object has been changed on other");
            }
        } else {
            jdbcTemplate.update(INSERT_STATE_SQL, IdUtil.getSnowflakeNextId(), stateData.getMachineId(),
                    stateData.getBizId(), state, stateData.isEnded(), operator, operator);
        }
    }

    @Override
    public StateData<S> read(String machineId, String bizId) {
        StateDO stateDO;
        try {
            stateDO = jdbcTemplate.queryForObject(SELECT_SQL, new DataClassRowMapper<>(StateDO.class), machineId, bizId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        if (stateDO == null) {
            return null;
        }
        StateData<S> stateData = new StateData<>();
        stateData.setId(stateDO.getId());
        stateData.setState(new State<>((S) type));
        stateData.setMachineId(stateDO.getMachineId());
        stateData.setBizId(stateDO.getBizId());
        stateData.setEnded(stateDO.getEnded() == 1);
        stateData.setExtras(Maps.newHashMap());
        return stateData;
    }

}
