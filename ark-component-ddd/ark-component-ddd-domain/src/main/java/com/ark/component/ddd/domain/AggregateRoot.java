package com.ark.component.ddd.domain;

import cn.hutool.core.util.IdUtil;
import com.ark.component.ddd.domain.event.DomainEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
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
        this.id = IdUtil.getSnowflakeNextId();
        this.createTime = LocalDateTime.now();
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
}

