package com.ark.component.tree;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;

public interface TreeService {

    /**
     * 添加树节点
     *
     * @return 节点id
     */
    Long addNode(TreeNode node);


    /**
     * 删除树节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     */
    void removeNode(String bizType, Long bizId);

    /**
     * 获取所有树节点
     */
    List<TreeNode> queryNodes(String bizType, List<Long> menuIds);


    /**
     *
     * @param bizType
     * @param data
     * @return
     * @param <T>
     */
    <T extends TreeDTO<Long>> List<Tree<Long>> queryTreeNodes(String bizType, List<T> data);
}
