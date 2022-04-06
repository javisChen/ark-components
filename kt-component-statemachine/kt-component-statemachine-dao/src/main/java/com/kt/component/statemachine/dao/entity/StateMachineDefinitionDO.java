package com.kt.component.statemachine.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kt.component.db.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 状态机规则定义表
 * </p>
 *
 * @author EOP
 * @since 2022-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stm_statemachine_definition")
public class StateMachineDefinitionDO extends BaseEntity {


    /**
     * 业务编码
     */
    @TableField("biz_code")
    private String bizCode;

    /**
     * 状态机规则配置（JSON）
     */
    @TableField("config")
    private String config;

    /**
     * 启用状态 0-禁用；1-启用；
     */
    @TableField("`status`")
    private Integer status;

}
