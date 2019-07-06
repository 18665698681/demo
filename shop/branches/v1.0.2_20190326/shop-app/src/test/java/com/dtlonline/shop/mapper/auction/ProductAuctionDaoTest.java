package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductAuctionDaoTest {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductAuctionRecordDao productAuctionRecordDao;

    @Test
    public void test() {
        ProductAuction productAuction = productAuctionDao.selectById(21);
        System.out.println(productAuction);
    }

    @Test
    public void testOrder(){
        QueryWrapper<ProductAuctionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("productId","1104");
        wrapper.orderByDesc("price", "quantity");
        wrapper.orderByAsc("createTime");
        List<ProductAuctionRecord> records = productAuctionRecordDao.selectList(wrapper);
    }
}