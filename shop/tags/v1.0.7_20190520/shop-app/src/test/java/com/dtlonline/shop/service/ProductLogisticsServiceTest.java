package com.dtlonline.shop.service;

import com.dtlonline.shop.model.ProductLogistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductLogisticsServiceTest {

    @Autowired
    private ProductLogisticsService productLogisticsService;

    @Test
    public void queryProductLogisticsForTxnId() throws Exception {
        Set set = new HashSet();
        set.add("1");
        Map<String,ProductLogistics> map = productLogisticsService.queryProductLogisticsForTxnId(set);
        System.out.println(map.get("1").getTxnId());
    }

}