package com.ark.component.tree;

import cn.hutool.core.lang.Assert;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@TableName("func_tree_node")
@Getter
@Setter
public class TreeNode extends BaseEntity {

    @TableField("application_id")
    private Long applicationId;

    @TableField("biz_id")
    private Long bizId;

    @TableField("parent_biz_id")
    private Long parentBizId;

    @TableField("biz_type")
    private String bizType;

    @TableField("level_path")
    private String levelPath;

    @TableField("level")
    private Integer level;

    @TableField("sequence")
    private Integer sequence;

    public TreeNode() {

    }


    public TreeNode(String bizType, Long bizId, Long parentBizId, Integer sequence) {
        this.bizId = bizId;
        this.parentBizId = parentBizId;
        this.bizType = bizType;
        this.sequence = sequence == null ? 0 : sequence;
    }

    public static TreeNode createTreeNode(String bizType, Long bizId, Long parentBizId, Integer sequence) {
        Assert.notNull(bizType, "bizType must not be null");
        Assert.notNull(bizType, "bizId must not be null");
        Assert.notNull(bizType, "parentBizId must not be null");
        return new TreeNode(bizType, bizId, parentBizId, sequence);
    }


}
