package com.dtlonline.shop.mapper.auction;

import com.dtlonline.shop.model.auction.ProductAuction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductAuctionDaoTest {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Test
    public void test() {
        ProductAuction productAuction = productAuctionDao.selectById(21);
        System.out.println(productAuction);
    }
}