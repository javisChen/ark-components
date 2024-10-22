package com.ark.component.tree;

import cn.hutool.core.lang.tree.Tree;
import com.ark.component.tree.dto.TreeDTO;

import java.util.List;

public interface TreeService {


    TreeNode addNode(String bizType, Long bizId, Long parentBizId, Integer sequence);


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
    List<TreeNode> queryNodes(String bizType, List<Long> bizIds);

    /**
     * 获取指定节点
     */
    TreeNode queryNode(String bizType, Long bizId);



    /**
     *
     * @param bizType
     * @param data
     * @return
     * @param <T>
     */
    <T extends TreeDTO<Long>> List<Tree<Long>> queryTreeNodes(String bizType, List<T> data);

    void move(String bizType, Long bizId, Long newParentBizId);

    List<TreeNode> queryChildNodes(String bizType, Long bizId);

    List<Long> queryChildNodeBizIds(String bizType, Long bizId);
}
