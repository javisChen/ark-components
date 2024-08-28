package com.ark.component.tree;

import java.util.List;

public interface TreeService<E extends TreeNode> {

    /**
     * 添加树节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return 节点id
     */
    Long addNode(E node);


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
    List<E> queryNodes(String bizType);


}
