package com.ark.component.ddd.domain;

import com.ark.component.ddd.domain.event.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot implements Identity {
    private static final int MAX_OPS_LOG_SIZE = 20;

    private Long id;//通过Snowflake算法生成
    private Long tenantId = 1L;//在多租户下，所有聚合根都需要一个tenantId来对应某个租户
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private Long creator;//创建人Id
    private Long modifier;//更新人MemberId
    private List<DomainEvent> events;//领域事件列表，用于临时存放完成某个业务流程中所发出的事件，会被BaseRepository保存到事件表中
    // private LinkedList<OpsLog> opsLogs;//操作日志

    public AggregateRoot() {
    }

    public AggregateRoot(Long id) {
        this.id = id;
    }

    public void raiseEvent(DomainEvent event) {
//        event.setArInfo(this);
        // 防止重复发送事件
        allEvents().add(event);
    }

    public void clearEvents() {
        this.events = null;
    }

    private List<DomainEvent> allEvents() {
        if (events == null) {
            this.events = new ArrayList<>();
        }
        return events;
    }

    @Override
    public Long getIdentifier() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public Long getId() {
        return id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Long getCreator() {
        return creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public List<DomainEvent> getEvents() {
        return events;
    }

    public void setEvents(List<DomainEvent> events) {
        this.events = events;
    }
}

