package com.kt.component.db.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_modified")
    private LocalDateTime gmtModified;

    @TableField(value = "creator")
    private Long creator;

    @TableField(value = "modifier")
    private Long modifier;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public BaseEntity() {
    }
}
