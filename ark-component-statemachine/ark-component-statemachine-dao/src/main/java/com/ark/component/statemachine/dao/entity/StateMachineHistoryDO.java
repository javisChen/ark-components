package com.ark.component.statemachine.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ark.component.orm.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 状态机历史表
 * </p>
 *
 * @author EOP
 * @since 2022-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stm_history")
public class StateMachineHistoryDO extends BaseEntity {


    /**
     * 业务编码
     */
    @TableField("biz_code")
    private String bizCode;

    /**
     * 业务ID
     */
    @TableField("biz_id")
    private Long bizId;

    /**
     * 驱动的事件
     */
    @TableField("`event`")
    private String event;

    /**
     * 转换前状态
     */
    @TableField("pre_state")
    private String preState;

    /**
     * 当前状态
     */
    @TableField("current_state")
    private String currentState;

}
