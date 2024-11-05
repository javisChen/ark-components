package com.ark.component.tree;

import cn.hutool.core.lang.tree.Tree;
import com.ark.component.tree.dto.HierarchyCommand;
import com.ark.component.tree.dto.HierarchyDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 封装一层方便上层调用的API
 */
@RequiredArgsConstructor
public abstract class BizTreeService {

    public abstract String bizType();

    private final TreeOperations treeOperations;

    public TreeNode addNode(HierarchyCommand command) {
        return treeOperations.addNode(bizType(), command.getId(), command.getParentId(), command.getSequence());
    }

    public <T extends HierarchyDTO<Long>> List<Tree<Long>> transformToTree(List<T> data) {
        return treeOperations.transformToTree(bizType(), data);
    }

    /**
     * 修改节点层级
     *
     * @param nodeId       节点id
     * @param parentNodeId 上级节点id
     */
    public void changeLevel(Long nodeId, Long parentNodeId) {
        treeOperations.move(bizType(), nodeId, parentNodeId);
    }

    /**
     * 查询子节点
     *
     * @param nodeId 节点id
     */
    public List<Long> queryChildNodeIds(Long nodeId) {
        List<TreeNode> treeNodes = treeOperations.queryChildNodes(bizType(), nodeId);
        return treeNodes.stream().map(TreeNode::getBizId).toList();
    }

    /**
     * 设置节点参数
     *
     * @param data 原业务对象
     */
    public <T extends HierarchyDTO<Long>> void populateNodeParams(T data) {
        TreeNode treeNode = treeOperations.queryNode(bizType(), data.getId());
        data.setLevelPath(treeNode.getLevelPath());
        data.setLevel(treeNode.getLevel());
        data.setParentId(treeNode.getParentBizId());
    }

    /**
     * 删除节点和其子节点
     *
     * @param nodeId 节点id
     * @return 返回被删掉节点业务id，方便上层删除业务表数据。
     */
    public List<Long> removeNodeAndChildren(Long nodeId) {
        return treeOperations.removeNodeAndChildren(bizType(), nodeId);
    }

}
