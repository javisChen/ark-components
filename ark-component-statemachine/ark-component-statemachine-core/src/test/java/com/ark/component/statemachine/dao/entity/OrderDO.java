package com.ark.component.statemachine.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ark.component.orm.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author EOP
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("od_order")
public class OrderDO extends BaseEntity {


    /**
     * 业务编码
     */
    @TableField("price")
    private Integer price;

    /**
     * 状态
     */
    @TableField("`status`")
    private Integer status;

}
