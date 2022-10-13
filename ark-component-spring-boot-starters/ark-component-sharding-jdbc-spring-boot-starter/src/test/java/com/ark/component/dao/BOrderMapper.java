package com.ark.component.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ark.component.po.BOrder;
import com.ark.component.po.Position;

import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author
 * @since 2020-11-09
 */
public interface BOrderMapper extends BaseMapper<BOrder> {

    Page<Position> selectAndDetail(Page page);

    Map<String, String> selectOrder(Long orderId);

}
