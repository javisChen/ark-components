package com.ark.component.tree;

import com.ark.component.orm.mybatis.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("func_tree_node")
public class TreeNode extends BaseEntity {

    @TableField("application_id")
    private Long applicationId;

    @TableField("biz_id")
    private Long bizId;

    @TableField("biz_type")
    private String bizType;

    @TableField("pid")
    private Long pid;

    @TableField("level_path")
    private String levelPath;

    @TableField("level")
    private Integer level;

    @TableField("sequence")
    private Integer sequence;


}
