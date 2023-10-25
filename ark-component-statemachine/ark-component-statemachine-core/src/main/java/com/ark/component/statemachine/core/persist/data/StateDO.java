package com.ark.component.statemachine.core.persist.data;

import java.time.LocalDateTime;

public class StateDO {

    private Long id;

    private String machineId;

    private String bizId;

    private Integer ended;

    private LocalDateTime gmtCreate;

    private Long creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Integer getEnded() {
        return ended;
    }

    public void setEnded(Integer ended) {
        this.ended = ended;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }
}
