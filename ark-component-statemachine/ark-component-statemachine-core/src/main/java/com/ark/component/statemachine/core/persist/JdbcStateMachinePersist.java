package com.ark.component.statemachine.core.persist;


import cn.hutool.core.util.IdUtil;
import com.ark.component.statemachine.core.StateData;
import com.ark.component.statemachine.core.StateMachineException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JdbcStateMachinePersist<S> implements StateMachinePersist<S> {
    private final Map<String, StateData<S>> repository = new HashMap<>(16);
    private final JdbcTemplate jdbcTemplate;

    private final static String INSERT_STATE_SQL = "insert into stm_state (id, machine_id, biz_id, state, ended, gmt_create, creator)" +
            "values (?, ?, ?, ?, ?, ?, ?)";

    private final static String UPDATE_STATE_SQL = "update stm_state set state = ? where id= ?";

    public JdbcStateMachinePersist(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(StateData<S> stateData) {
        if (stateData.getId() != null) {
            int updated = jdbcTemplate.update(UPDATE_STATE_SQL,
                    IdUtil.getSnowflakeNextId(),
                    stateData.getState(),
                    stateData.getExtras(),
                    stateData.isEnded(),
                    LocalDateTime.now(),
                    0L);
            if (updated == 0) {
                throw new StateMachineException("State object has been changed on other");
            }
        } else {
            int updated = jdbcTemplate.update(INSERT_STATE_SQL,
                    IdUtil.getSnowflakeNextId(),
                    stateData.getMachineId(),
                    stateData.getBizId(),
                    stateData.getState(),
                    stateData.getExtras(),
                    stateData.isEnded(),
                    LocalDateTime.now(),
                    0L);
            if (updated == 0) {
                throw new StateMachineException("State object has been changed on other");
            }
        }
    }

    @Override
    public StateData<S> read(String id) {
        return repository.get(id);
    }
}
