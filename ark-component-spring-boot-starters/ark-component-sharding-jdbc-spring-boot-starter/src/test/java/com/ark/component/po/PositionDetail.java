package com.ark.component.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("position_detail")
public class PositionDetail {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("pid")
    private Long pid;
    @TableField("description")
    private String description;

}
