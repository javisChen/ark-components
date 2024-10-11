package com.ark.component.tree;

import java.util.List;
import java.util.function.Consumer;

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


//    List<TreeNode> queryTreeNodes(String bizType, List<Long> menuIds);

    List<TreeNode> queryTreeNodes(String bizType, List<Long> menuIds, Consumer<TreeNode> treeNodeConsumer);
}
