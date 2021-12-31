package com.kt.component.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("b_order")
public class BOrder {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("is_del")
    private String isDel;
    @TableField("company_id")
    private Integer companyId;
    @TableField("order_id")
    private Long orderId;
    @TableField("position_id")
    private Long positionId;
    @TableField("user_id")
    private Integer userId;
    @TableField("publish_user_id")
    private Integer publishUserId;
    @TableField("resume_type")
    private Integer resumeType;
    @TableField("status")
    private String status;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("operate_time")
    private LocalDateTime operateTime;
    @TableField("work_year")
    private String workYear;
    @TableField("name")
    private String name;
    @TableField("position_name")
    private String positionName;
    @TableField("resume_id")
    private Integer resumeId;


}
