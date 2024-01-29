package com.ark.component.ddd.domain.event;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.ark.component.ddd.domain.AggregateRoot;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.EventObject;

import static java.time.Instant.now;


@Getter
@Setter
public class DomainEvent extends EventObject {

    @TableId
    private Long id;

    /**
     * 租户ID
     */
    @TableField
    private Long tenantId;//事件对应的租户ID，不能为空

    /**
     * 聚合根ID
     */
    @TableField
    private Long arId;

    /**
     * 事件类型
     */
    @TableField
    private String type;

    /**
     * @see DomainEventStatus
     */
    @TableField
    private DomainEventStatus status;

    /**
     * 已经发布的次数，无论成功与否
     */
    @TableField
    private int publishedCount;
    /**
     * 已经被消费的次数，无论成功与否
     */
    @TableField
    private int consumedCount;

    /**
     * 触发事件的用户ID
     */
    @TableField
    private Long triggeredBy;

    /**
     * 触发事件的时间
     */
    @TableField
    private Instant triggeredAt;

    /**
     * 触发事件的时间
     */
    @TableField
    private String eventData;

    protected DomainEvent(Object source, String type, Long userId) {
        super(source);
        Assert.notNull(type, "Domain event type must not be null.");
        this.id = newEventId();
        this.type = type;
        this.status = DomainEventStatus.CREATED;
        this.publishedCount = 0;
        this.consumedCount = 0;
        this.triggeredBy = userId;
        this.triggeredAt = now();
    }

    public DomainEvent(Object source, String type) {
        super(source);
        Assert.notBlank(type, "Domain event type must not be null.");
        this.id = newEventId();
        this.type = type;
        this.status = DomainEventStatus.CREATED;
        this.publishedCount = 0;
        this.consumedCount = 0;
        this.triggeredAt = now();
    }

    public Long newEventId() {
        return IdUtil.getSnowflakeNextId();
    }

    public void setArInfo(AggregateRoot ar) {
        this.tenantId = ar.getTenantId();
        this.arId = ar.getId();
    }

    public boolean isConsumedBefore() {
        return this.consumedCount > 0;
    }

    public boolean isNotConsumedBefore() {
        return !isConsumedBefore();
    }

}
