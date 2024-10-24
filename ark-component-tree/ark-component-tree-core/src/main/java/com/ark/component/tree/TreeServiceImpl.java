package com.ark.component.tree;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import com.ark.component.exception.ExceptionFactory;
import com.ark.component.orm.mybatis.base.BaseEntity;
import com.ark.component.tree.dao.TreeNodeMapper;
import com.ark.component.tree.dto.TreeDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreeServiceImpl extends ServiceImpl<TreeNodeMapper, TreeNode> implements TreeService {

    private final static String PATH_SEPARATOR = "/";

    private final static long DEFAULT_ROOT_PID = 0L;

    @Override
    public TreeNode addNode(String bizType, Long bizId, Long parentBizId, Integer sequence) {
        TreeNode treeNode = TreeNode.createTreeNode(bizType, bizId, parentBizId, sequence);
        return saveNode(treeNode);
    }

    /**
     * 保存节点，节点会根据父级节点自动计算出当前层级、层级路径等
     */
    private TreeNode saveNode(TreeNode treeNode) {
        if (treeNode.getId() == null) {
            treeNode.setId(IdUtil.getSnowflakeNextId());
        }

        TreeNode parentNode = getParentNode(treeNode);
        Long nodeBizId = treeNode.getBizId();
        // 根据parent信息计算出当前层级信息
        String levelPath = parentNode == null
                ? nodeBizId + PATH_SEPARATOR
                : parentNode.getLevelPath() + nodeBizId + PATH_SEPARATOR;
        treeNode.setParentBizId(parentNode != null ? parentNode.getBizId() : 0);
        treeNode.setLevelPath(levelPath);
        treeNode.setLevel(parentNode != null ? parentNode.getLevel() + 1 : 1);

        saveOrUpdate(treeNode);
        return treeNode;
    }

    private TreeNode getParentNode(TreeNode treeNode) {
        TreeNode parentNode = null;
        Long parentBizId = treeNode.getParentBizId();
        if (parentBizId == null || parentBizId.equals(DEFAULT_ROOT_PID)) {
            return parentNode;
        }
        parentNode = queryNode(treeNode.getBizType(), parentBizId);
        Assert.notNull(parentNode, ExceptionFactory.userExceptionSupplier("The parent node does not exist"));
        return parentNode;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void removeNode(String bizType, Long bizId) {
        TreeNode node = queryNode(bizType, bizId);
        if (node == null) {
            return;
        }
        log.info("Starting remove tree nodes, bizType = {}, bizId = {}", bizType, bizId);
        String levelPath = node.getLevelPath();
        List<TreeNode> children = queryChildNodes(levelPath);
        List<Long> willDeleteIds = new ArrayList<>(1);
        if (CollUtil.isNotEmpty(children)) {
            willDeleteIds = new ArrayList<>(children.size() + 1);
            willDeleteIds.addAll(children.stream().map(BaseEntity::getId).toList());
        }

        log.info("Path = {}, {} nodes will be removed", levelPath, willDeleteIds.size());
        List<Long> ids = willDeleteIds.stream().sorted().collect(Collectors.toList());
        lambdaUpdate()
                .in(BaseEntity::getId, ids)
                .remove();
    }

    @Override
    public List<TreeNode> queryNodes(String bizType, List<Long> bizIds) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .in(TreeNode::getBizId, bizIds)
                .list();
    }

    @Override
    public TreeNode queryNode(String bizType, Long bizId) {
        return lambdaQuery()
                .eq(TreeNode::getBizType, bizType)
                .eq(TreeNode::getBizId, bizId)
                .last("limit 1")
                .one();
    }

    @Override
    public <T extends TreeDTO<Long>> List<Tree<Long>> transformToTree(String bizType, List<T> data) {
        if (CollectionUtils.isEmpty(data)) {
            return Collections.emptyList();
        }
        List<TreeNode> treeNodes = queryNodes(bizType, data.stream().map(T::getId).toList());
        Map<Long, T> dataMap = data.stream().collect(Collectors.toMap(T::getId, menu -> menu));
        return TreeUtil.build(treeNodes, DEFAULT_ROOT_PID, (TreeNode treeNode, Tree<Long> tree) -> {
            if (dataMap.containsKey(treeNode.getBizId())) {
                T bizNode = dataMap.get(treeNode.getBizId());
                bizNode.setLevelPath(treeNode.getLevelPath());
                bizNode.setLevel(treeNode.getLevel());
                bizNode.setParentId(treeNode.getParentBizId());
                tree.setName(bizNode.getName());
                tree.setWeight(treeNode.getSequence());
                tree.putAll(BeanUtil.beanToMap(bizNode));
            }
            tree.setId(treeNode.getBizId());
            tree.setParentId(treeNode.getParentBizId());
        });

    }


    @Override
    public <T extends TreeDTO<Long>> T transformToTreeNode(String bizType, T data) {
        TreeNode treeNode = queryNode(bizType, data.getId());
        data.setLevelPath(treeNode.getLevelPath());
        data.setLevel(treeNode.getLevel());
        data.setParentId(treeNode.getParentBizId());
        return data;
    }

    @Override
    public void move(String bizType, Long bizId, Long newParentBizId) {

        TreeNode treeNode = queryNode(bizType, bizId);

        // 如果pid不一致的话才做更新
        if (treeNode.getParentBizId().equals(newParentBizId)) {
            return;
        }

        Integer level = treeNode.getLevel();
        String levelPath = treeNode.getLevelPath();

        treeNode.setParentBizId(newParentBizId);
        saveNode(treeNode);

        Integer currentLevel = treeNode.getLevel();
        moveChildNodes(currentLevel, level, levelPath);
    }

    @Override
    public List<TreeNode> queryChildNodes(String bizType, Long bizId) {
        TreeNode treeNode = queryNode(bizType, bizId);
        return queryChildNodes(treeNode.getLevelPath());
    }

    @Override
    public List<Long> queryChildNodeBizIds(String bizType, Long bizId) {
        List<TreeNode> treeNodes = queryChildNodes(bizType, bizId);
        return treeNodes.stream().map(TreeNode::getBizId).toList();
    }

    private void moveChildNodes(Integer currentLevel, Integer oldLevel, String levelPath) {
        List<TreeNode> children = queryChildNodes(levelPath);
        // 计算原等级和新等级差值
        int diff = currentLevel - oldLevel;
        List<TreeNode> entityList = new ArrayList<>();
        for (TreeNode child : children) {
            TreeNode node = new TreeNode();
            node.setId(child.getId());
            node.setLevel(child.getLevel() + diff);
            node.setLevelPath(createChildLevelPath(child, levelPath));
            entityList.add(node);
        }
        updateBatchById(entityList);
    }

    /**
     * 查询所有子节点
     */
    private List<TreeNode> queryChildNodes(String parentLevelPath) {
        return lambdaQuery()
                .select(TreeNode::getId,
                        TreeNode::getBizId,
                        TreeNode::getBizType,
                        TreeNode::getParentBizId,
                        TreeNode::getLevel,
                        TreeNode::getLevelPath)
                .likeRight(TreeNode::getLevelPath, parentLevelPath)
                .list();
    }

    /**
     * 生成层级路径
     * 实现逻辑：
     * 1.例如当前child的id=101，层级路径是301/302/101
     * 2.新的parent路径是401/402/
     * 3.把301/302/ 替换成 401/402/ 即可完成移动
     */
    private String createChildLevelPath(TreeNode child,
                                        String newParentPath) {
        // 如果当前路由是"64.65.66." 父路由id是"65"，那么该值就是"64."，把该值替换成当前修改的父级路由的level_path
        String oldParentPath = StringUtils.substringBefore(child.getLevelPath(), String.valueOf(child.getBizId()));
        return StringUtils.replace(child.getLevelPath(), oldParentPath, newParentPath);
    }

    public static void main(String[] args) {
//        TreeNode treeNode = TreeNode.createTreeNode();
//        treeNode.setBizId(3L);
//        treeNode.setLevelPath("1/2/3");
//        String childLevelPath = new TreeServiceImpl().createChildLevelPath(treeNode, "11/22/");
//        System.out.println(childLevelPath);
    }

}
