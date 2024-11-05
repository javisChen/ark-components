package com.ark.component.tree;

import cn.hutool.core.lang.tree.Tree;
import com.ark.component.tree.dto.HierarchyDTO;

import java.util.List;

/**
 * 树形结构操作接口
 */
public interface TreeOperations {


    /**
     * 添加节点
     */
    TreeNode addNode(String bizType, Long bizId, Long parentBizId, Integer sequence);


    /**
     * 删除节点和其子节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     * @return 返回被删掉节点业务id，方便上层删除业务表数据。
     */
    List<Long> removeNodeAndChildren(String bizType, Long bizId);

    /**
     * 获取所有树节点
     *
     * @param bizType 业务类型
     * @param bizIds  业务id
     */
    List<TreeNode> queryNodes(String bizType, List<Long> bizIds);

    /**
     * 获取指定节点
     *
     * @param bizType 业务类型
     * @param bizId   业务id
     */
    TreeNode queryNode(String bizType, Long bizId);

    /**
     * 移动节点层级
     *
     * @param bizType        业务类型
     * @param bizId          业务id
     * @param newParentBizId 新的上级节点id
     */
    void move(String bizType, Long bizId, Long newParentBizId);

    /**
     * 查询子节点
     *
     * @param bizType        业务类型
     * @param bizId          业务id
     */
    List<TreeNode> queryChildNodes(String bizType, Long bizId);

    /**
     * 查询子节点业务id集合
     * @param bizType        业务类型
     * @param bizId          业务id
     */
    List<Long> queryChildNodeBizIds(String bizType, Long bizId);

    /**
     * 把业务对象填充层级信息
     *
     * @param bizType 业务类型
     * @param data    原始业务对象
     */
    <T extends HierarchyDTO<Long>> T transformToTreeNode(String bizType, T data);


    /**
     * 把扁平化的原始数据转换成树形结构数据
     *
     * @param bizType 业务类型
     * @param data    原始数据
     */
    <T extends HierarchyDTO<Long>> List<Tree<Long>> transformToTree(String bizType, List<T> data);
}
