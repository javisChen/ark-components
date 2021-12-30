package com.kt.component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kt.component.dao.PositionMapper;
import com.kt.component.po.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {TestApp.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("sharding-database") // 分库
public class ShardingTableTest {

    @Autowired
    private PositionMapper positionMapper;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testQuery() {
        Page<Position> orders = positionMapper.selectPage(new Page<>(2, 20), null);
        System.out.println(orders.getRecords());
    }


    @Test
    public void testAdd() {
        for (int i = 0; i < 10; i++) {
            Position entity = new Position();
            entity.setName("Position" + i);
            entity.setSalary("1000000");
            entity.setCity("GZ");
            positionMapper.insert(entity);
        }
    }

}