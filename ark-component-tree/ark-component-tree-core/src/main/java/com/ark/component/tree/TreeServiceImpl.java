package com.ark.component.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.ark.component.exception.ExceptionFactory;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.ark.component.tree.dao.TreeNodeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreeServiceImpl extends ServiceImpl<TreeNodeMapper, TreeNode> implements TreeService {

    private final static String PATH_SEPARATOR = "/";

    @Override
    public Long addNode(TreeNode treeNode) {

        if (treeNode.getId() == null) {
            treeNode.setId(IdUtil.getSnowflakeNextId());
        }

        TreeNode parentNode = getParentNode(treeNode);

        Long nodeId = treeNode.getId();

        String levelPath = parentNode == null
                ? nodeId + PATH_SEPARATOR
                : parentNode.getLevelPath() + nodeId + PATH_SEPARATOR;

        treeNode.setParentBizId(parentNode != null ? parentNode.getBizId() : 0);
        treeNode.setLevelPath(levelPath);
        treeNode.setLevel(parentNode != null ? parentNode.getLevel() + 1 : 1);
        save(treeNode);
        return treeNode.getId();
    }

    private TreeNode getParentNode(TreeNode treeNode) {
        TreeNode parentNode = null;
        Long parentBizId = treeNode.getParentBizId();
        if (parentBizId != null && parentBizId != 0) {
            parentNode = getNode(treeNode.getBizType(), parentBizId);
            Assert.notNull(parentNode, ExceptionFactory.userExceptionSupplier("The parent node does not exists"));
        }
        return parentNode;
    }

    private TreeNode getNode(String bizType, Long bizId) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .eq(TreeNode::getBizId, bizId)
                .one();
    }

    private TreeNode getNode(TreeNode treeNode) {
        return lambdaQuery()
                .eq(TreeNode::getBizId, treeNode)
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
                .select(TreeNode::getId)
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
    public List<TreeNode> queryNodes(String bizType, List<Long> menuIds) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .in(TreeNode::getBizId, menuIds)
                .list();
    }

    @Override
    public List<TreeNode> queryTreeNodes(String bizType, List<Long> menuIds, Consumer<TreeNode> treeNodeConsumer) {

        List<TreeNode> treeNodes = queryNodes("MENU", menuIds);

        // page 转成Map以id为key
//        Map<Long, MenuDTO> menuMap = menuDTO.stream().collect(Collectors.toMap(MenuDTO::getId, menu -> menu));

        return null;
//        return TreeUtil.build(treeNodes, 0L, (TreeNode treeNode, Tree<Long> tree) -> {
//            tree.setId(treeNode.getId());
//            tree.setParentId(treeNode.getParentBizId());
//            treeNodeConsumer.accept(treeNode);
//            if (menuMap.containsKey(treeNode.getBizId())) {
//                MenuDTO menu = menuMap.get(treeNode.getBizId());
//                tree.setName(menu.getName());
//                tree.putAll(BeanUtil.beanToMap(menu));
//            }
//        });
    }

}
