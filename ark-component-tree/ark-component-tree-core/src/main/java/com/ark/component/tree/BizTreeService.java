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

    public void changeLevel(Long nodeId, Long parentNodeId) {
        treeOperations.move(bizType(), nodeId, parentNodeId);
    }

    public List<Long> queryChildNodeIds(Long nodeId) {
        return treeOperations.queryChildNodeBizIds(bizType(), nodeId);
    }

    public <T extends HierarchyDTO<Long>> T transformToTreeNode(T data) {
        return treeOperations.transformToTreeNode(bizType(), data);
    }

    public List<Long> removeNodeAndChildren(Long nodeId) {
        return treeOperations.removeNodeAndChildren(bizType(), nodeId);
    }

}
