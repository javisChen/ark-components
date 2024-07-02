package com.ark.component.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.IdUtil;
import com.ark.component.exception.ExceptionFactory;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.incrementer.SapHanaKeyGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


//@Service
@RequiredArgsConstructor
public class AbstractTreeService<E extends TreeNode> extends ServiceImpl<BaseMapper<E>, E> implements TreeService {

    private final static String PATH_SEPARATOR = "/";

    @Override
    public Long addNode(TreeNode node) {
        TreeNode parent = null;

        if (node.getId() == null) {
            node.setId(IdUtil.getSnowflakeNextId());
        }

        if (node.getPid() != null) {
            parent = getNode(node.getPid());
            Assert.notNull(parent, ExceptionFactory.userExceptionSupplier("父节点不存在"));
        }

        Long nodeId = node.getId();
        String levelPath = parent == null
                ? nodeId + PATH_SEPARATOR
                : parent.getLevelPath() + nodeId + PATH_SEPARATOR;
        Long parentId = parent != null ? parent.getId() : 0;
        int level = parent != null ? parent.getLevel() + 1 : 0;

        E treeNode = null;
        try {

            treeNode = getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        treeNode.setApplicationId(0L);
        treeNode.setPid(parentId);
        treeNode.setLevelPath(levelPath);
        treeNode.setLevel(level);
        treeNode.setId(0L);
        save(treeNode);
        return treeNode.getId();
    }

    private E getNode(Long bizId) {
        return lambdaQuery()
                .eq(E::getId, bizId)
                .one();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeNode(String bizType, Long bizId) {

        E node = getNode(bizId);
        if (node == null) {
            return;
        }
        List<E> children = lambdaQuery()
                .select(E::getId)
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
    public List<E> queryNodes(String bizType) {
        return lambdaQuery()
                .list();
    }
}
