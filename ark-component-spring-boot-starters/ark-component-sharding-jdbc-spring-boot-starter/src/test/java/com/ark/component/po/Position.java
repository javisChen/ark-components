package com.ark.component.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("position")
public class Position {

    /**
     * IdType.ASSIGN_ID就会用MybatisPlus自带的雪花
     * IdType.Auto 使用Auto的话，sharding-jdbc检测到没有主动插入id的话，就会调用自带的分片序列生成
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("name")
    private String name;
    @TableField("salary")
    private String salary;
    @TableField("city")
    private String city;

}
