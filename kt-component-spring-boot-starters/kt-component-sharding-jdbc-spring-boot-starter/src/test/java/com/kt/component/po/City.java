package com.kt.component.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("city")
public class City {

    @TableId("Id")
    private Integer Id;
    @TableField("name")
    private String name;
    @TableField("province")
    private String province;

}
