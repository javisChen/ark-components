package com.ark.component.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("b_order_item")
public class BOrderItem {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("pid")
    private Long pid;
    @TableField("description")
    private String description;
    @TableField("company_id")
    private Integer companyId;
    @TableField("order_id")
    private Long orderId;

}
