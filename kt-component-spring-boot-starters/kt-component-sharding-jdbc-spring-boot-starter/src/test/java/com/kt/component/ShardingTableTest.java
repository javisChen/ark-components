package com.kt.component;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dao.*;
import com.kt.component.po.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 分库分表
 */
@SpringBootTest(classes = {TestApp.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("sharding-database") // 分库
public class ShardingTableTest {

    @Autowired
    private PositionMapper positionMapper;
    @Autowired
    private PositionDetailMapper positionDetailMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private BOrderMapper bOrderMapper;
    @Autowired
    private BOrderItemMapper bOrderItemMapper;

    private SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator(1, 1);

    @Before
    public void setUp() {

    }

    @Test
    public void testQuery() {
        Page<Position> orders = positionMapper.selectAndDetail(new Page<>(1, 20));
        System.out.println(orders.getRecords());
    }

    @Test
    public void testQueryOne() {
        Map<String, String> orders = positionMapper.selectPosition(1476513649509142530L);
        System.out.println(orders);
    }

    @Test
    public void testQueryOrders() {
        Page<Position> orders = bOrderMapper.selectAndDetail(new Page<>(1, 10));
        System.out.println(orders.getRecords());
    }

    @Test
    public void testQueryOrderAndItem() {
        Map<String, String> orders = bOrderMapper.selectOrder(1476788934550540290L);
        System.out.println(orders);
    }


    @Test
    public void testQueryOrder() {
        BOrder orders = bOrderMapper.selectById(1476788934550540290L);
        System.out.println(orders);
    }

    @Test
    public void testAddBoardCast() {
        City city = new City();
        city.setName("佛山");
        city.setProvince("广东省");
        cityMapper.insert(city);
    }

    @Test
    public void testQueryBoardCast() {
        List<City> cities = cityMapper.selectList(null);
        for (City city1 : cities) {
            System.out.println(city1);
        }
    }

    @Test
    public void testAddShardingBOrder() {
        for (int i = 0; i < 50; i++) {
            Long next = snowflakeGenerator.next();
            BOrder bOrder = new BOrder();
            bOrder.setIsDel("");
            bOrder.setCompanyId(i);
            bOrder.setOrderId(next);
            bOrder.setPositionId(0L);
            bOrder.setUserId(0);
            bOrder.setPublishUserId(0);
            bOrder.setResumeType(0);
            bOrder.setStatus("");
            bOrder.setCreateTime(LocalDateTime.now());
            bOrder.setOperateTime(LocalDateTime.now());
            bOrder.setWorkYear("2020-02");
            bOrder.setName("买书订单" + i);
            bOrder.setPositionName("");
            bOrder.setResumeId(0);
            bOrderMapper.insert(bOrder);

            BOrderItem orderItem = new BOrderItem();
            orderItem.setPid(bOrder.getId());
            orderItem.setOrderId(bOrder.getOrderId());
            orderItem.setCompanyId(bOrder.getCompanyId());
            bOrderItemMapper.insert(orderItem);
        }
    }

    @Test
    public void testQueryShardingBOrder() {
        Page<BOrder> bOrderPage = bOrderMapper.selectPage(new Page<>(1, 10), null);
        for (BOrder record : bOrderPage.getRecords()) {
            System.out.println(record);
        }
    }

    @Test
    public void testAddPosition() {
        for (int i = 0; i < 10; i++) {
            Position position = new Position();
            position.setName("Position" + i);
            position.setSalary("1000000");
            position.setCity("GZ");
            positionMapper.insert(position);

            PositionDetail positionDetail = new PositionDetail();
            positionDetail.setPid(position.getId());
            positionDetail.setDescription(position.getName() + "Detail");
            positionDetailMapper.insert(positionDetail);
        }
    }

}