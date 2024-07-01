package com.ark.component.tree;

import java.util.List;

public interface TreeService {

    /**
     * 添加树节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return 节点id
     */
    Long addNode(String bizType, Long bizId);

    /**
     * 添加树节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @param pid     父id
     * @return 节点id
     */
    Long addNode(String bizType, Long bizId, Long pid);


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
    List<TreeNode> queryNodes(String bizType);


}
