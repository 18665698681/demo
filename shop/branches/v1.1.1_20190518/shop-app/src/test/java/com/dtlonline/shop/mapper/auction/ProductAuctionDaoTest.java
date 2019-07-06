package com.dtlonline.shop.mapper.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.service.auction.AuctionCenterService;
import com.dtlonline.shop.service.auction.AuctionQueryService;
import com.dtlonline.shop.service.task.AuctionTaskService;
import com.dtlonline.shop.view.auction.ProductAuctionDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductAuctionDaoTest {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductAuctionRecordDao productAuctionRecordDao;

    @Autowired
    private AuctionTaskService auctionTaskService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    @Autowired
    private AuctionCenterService auctionCenterService;


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

    @Test
    public void testNewOrder(){
        /*int qty = auctionCenterService.getDownSumQtyByProductId(1072L,new BigDecimal(1000),new BigDecimal(6000));
        List<ProductAuctionRecord> records = auctionCenterService.selectDownSuccessList(1072L,new BigDecimal(1000),new BigDecimal(6000));
        records.forEach(record->{
            System.out.println("record"+record.toString());
        });*/
        //Set<Long> records = productAuctionRecordDao.selectDownNewUserRecordList(1072L);
        /*QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id",records);
        wrapper.orderByDesc("price", "quantity");
        wrapper.orderByAsc("createTime");
        List<ProductAuctionRecord> records3 = productAuctionRecordDao.selectList(wrapper);*/
       // List<ProductAuctionRecord> records2 =  productAuctionRecordDao.selectDownSuccessList(records,new BigDecimal(1000),new BigDecimal(6000));
        //System.out.println(records.size());
    }
    @Test
    public void test2NewOrder(){
        auctionTaskService.taskCreationOrder();
    }

    @Test
    public void queryProductAuctionByProductIds(){
       // System.out.println(auctionCenterService.getAuctionUserNo());

    }
}