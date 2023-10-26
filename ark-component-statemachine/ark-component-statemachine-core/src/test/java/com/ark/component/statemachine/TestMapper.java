package com.ark.component.statemachine;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TestMapper implements BaseMapper<String> {
    @Override
    public int insert(String entity) {
        return 0;
    }

    @Override
    public int deleteById(Serializable id) {
        return 0;
    }

    @Override
    public int deleteById(String entity) {
        return 0;
    }

    @Override
    public int deleteByMap(Map<String, Object> columnMap) {
        return 0;
    }

    @Override
    public int delete(Wrapper<String> queryWrapper) {
        return 0;
    }

    @Override
    public int deleteBatchIds(Collection<?> idList) {
        return 0;
    }

    @Override
    public int updateById(String entity) {
        return 0;
    }

    @Override
    public int update(String entity, Wrapper<String> updateWrapper) {
        return 0;
    }

    @Override
    public String selectById(Serializable id) {
        return null;
    }

    @Override
    public List<String> selectBatchIds(Collection<? extends Serializable> idList) {
        return null;
    }

    @Override
    public List<String> selectByMap(Map<String, Object> columnMap) {
        return null;
    }

    @Override
    public Long selectCount(Wrapper<String> queryWrapper) {
        return null;
    }

    @Override
    public List<String> selectList(Wrapper<String> queryWrapper) {
        return null;
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<String> queryWrapper) {
        return null;
    }

    @Override
    public List<Object> selectObjs(Wrapper<String> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<String>> P selectPage(P page, Wrapper<String> queryWrapper) {
        return null;
    }

    @Override
    public <P extends IPage<Map<String, Object>>> P selectMapsPage(P page, Wrapper<String> queryWrapper) {
        return null;
    }
}
