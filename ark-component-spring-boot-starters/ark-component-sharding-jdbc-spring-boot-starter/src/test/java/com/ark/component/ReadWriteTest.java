package com.ark.component;

import com.ark.component.dao.CityMapper;
import com.ark.component.po.City;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 读写分离
 */
@SpringBootTest(classes = {TestApp.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("readwrite") // 分库
public class ReadWriteTest {

    @Autowired
    private CityMapper cityMapper;

    @Before
    public void setUp() {

    }

    @Test
    public void testAdd() {
        City city = new City();
        city.setName("东莞");
        city.setProvince("广东省");
        cityMapper.insert(city);
    }

    @Test
    public void testQuery() {
        List<City> cities = cityMapper.selectList(null);
        for (City city : cities) {
            System.out.println(city.getName());
        }
    }

    /**
     * 强制走主库
     */
    @Test
    public void testQueryMasterOnly() {
        HintManager hintManager = HintManager.getInstance();
        hintManager.setWriteRouteOnly();
        List<City> cities = cityMapper.selectList(null);
        for (City city : cities) {
            System.out.println(city.getName());
        }
        hintManager.close();
    }

}