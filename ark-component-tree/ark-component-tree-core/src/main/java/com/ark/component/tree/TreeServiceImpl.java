package com.ark.component.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.ark.component.exception.ExceptionFactory;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.ark.component.tree.dao.TreeNodeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TreeServiceImpl extends ServiceImpl<TreeNodeMapper, TreeNode> implements TreeService {

    private final static String PATH_SEPARATOR = "/";

    @Override
    public Long addNode(String bizType, Long bizId) {
        return addNode(bizType, bizId, null);
    }

    @Override
    public Long addNode(String bizType, Long bizId, Long pid) {
        TreeNode parent = null;

        if (pid != null) {
            parent = getNode(bizType, pid);
            Assert.notNull(parent, ExceptionFactory.userExceptionSupplier("父节点不存在"));
        }

        String levelPath = parent == null
                ? bizId + PATH_SEPARATOR
                : parent.getLevelPath() + bizId + PATH_SEPARATOR;
        Long parentId = parent != null ? parent.getId() : 0;
        int level = parent != null ? parent.getLevel() + 1 : 0;

        TreeNode treeNode = new TreeNode();
        treeNode.setApplicationId(0L);
        treeNode.setBizId(bizId);
        treeNode.setBizType(bizType);
        treeNode.setPid(parentId);
        treeNode.setLevelPath(levelPath);
        treeNode.setLevel(level);
        treeNode.setId(0L);
        return null;
    }

    private TreeNode getNode(String bizType, Long bizId) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .eq(TreeNode::getBizId, bizId)
                .one();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeNode(String bizType, Long bizId) {

        TreeNode node = getNode(bizType, bizId);
        if (node == null) {
            return;
        }
        List<TreeNode> children = lambdaQuery()
                .select(BaseEntity::getId)
                .likeRight(TreeNode::getLevelPath, node.getLevelPath())
                .list();
        List<Long> willDeleteIds;
        if (CollUtil.isNotEmpty(children)) {
            willDeleteIds = new ArrayList<>(children.size() + 1);
            willDeleteIds.addAll(children.stream().map(BaseEntity::getId).toList());
        } else {
            willDeleteIds = new ArrayList<>(1);
        }

        List<Long> ids = willDeleteIds.stream().sorted().collect(Collectors.toList());
        lambdaUpdate().in(BaseEntity::getId, ids).remove();

    }

    @Override
    public List<TreeNode> queryNodes(String bizType) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .list();
    }
}
