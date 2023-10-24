package com.ark.component.statemachine.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ark.component.orm.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 状态机运行时表
 * </p>
 *
 * @author EOP
 * @since 2022-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stm_state")
public class StateDO extends BaseEntity {

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
     * 状态
     */
    @TableField("state")
    private String state;

    /**
     * 完结状态 0-否 1-是
     */
    @TableField("ended")
    private Boolean ended;

}
