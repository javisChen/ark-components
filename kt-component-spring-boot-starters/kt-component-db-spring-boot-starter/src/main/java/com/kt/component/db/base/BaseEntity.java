package com.kt.component.db.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @TableField(value = "creator", fill = FieldFill.INSERT)
    private Long creator;

    @TableField(value = "modifier", fill = FieldFill.INSERT_UPDATE)
    private Long modifier;

}
