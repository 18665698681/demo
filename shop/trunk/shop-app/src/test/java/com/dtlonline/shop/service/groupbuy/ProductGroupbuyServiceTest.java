package com.dtlonline.shop.service.groupbuy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductGroupbuyServiceTest {

    @Autowired
    private ProductGroupbuyService productGroupbuyService;

    @Test
    public void queryListInPage() {
         productGroupbuyService.queryListInPage(1,5,2);
    }

    @Test
    public void queryBrandDTOs() {
        //productGroupbuyService.queryBrandDTOs(7L);
    }
}