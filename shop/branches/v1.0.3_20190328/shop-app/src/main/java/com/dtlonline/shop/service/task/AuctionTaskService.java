package com.dtlonline.shop.service.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.order.command.BuyerAuctionOrder;
import com.dtlonline.api.order.remote.OrderRemoteService;
import com.dtlonline.shop.constant.auction.AuctionApplyStatus;
import com.dtlonline.shop.constant.auction.AuctionType;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionRecordDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionUserDao;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuctionTaskService extends BaseService implements AuctionApplyStatus {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductAuctionUserDao productAuctionUserDao;

    @Autowired
    private ProductAuctionRecordDao productAuctionRecordDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderRemoteService orderRemoteService;


    /**
     * @Author gaoqiang
     * @Description 定时任务产生竞拍订单 5分钟执行一次
     * @Date 10:11 2019/3/11
     **/
    @Scheduled(cron = "0 0/5 * * * ?")
    public void taskCreationOrder() {
        logger.info("定时任务生成订单 begin......");
        List<ProductAuction> auctions = productAuctionDao.selectByOver();
        auctions.forEach(auction -> {
            logger.info("[{}]定时任务[{}]生成订单{}", new Date(), auction.getProductId(),auction);
            Product product = productDao.selectById(auction.getProductId());
            int quantitys = 0;
            if(AuctionType.UP.getCode().equals(auction.getAuctionType())){
                quantitys = productAuctionRecordDao.selectUpSumQtyByProductId(auction.getProductId(),product.getUnitPrice());
            }else if(AuctionType.DOWN.getCode().equals(auction.getAuctionType())){
                quantitys = getDownSumQtyByProductId(auction.getProductId(),auction.getFloorPrice(),product.getUnitPrice());
            }
            QueryWrapper<ProductAuctionRecord> wrapper = new QueryWrapper<>();
            wrapper.eq("productId", auction.getProductId());
            wrapper.eq("buildOrder", 1);
            wrapper.ge("price",product.getUnitPrice());
            wrapper.orderByDesc("price", "quantity");
            wrapper.orderByAsc("createTime");
            List<ProductAuctionRecord> records = new ArrayList<>();
            if(AuctionType.UP.getCode().equals(auction.getAuctionType())){
                records = productAuctionRecordDao.selectList(wrapper);
            }else if(AuctionType.DOWN.getCode().equals(auction.getAuctionType())){
                records = selectDownSuccessList(auction.getProductId(),auction.getFloorPrice(),product.getUnitPrice());
            }
            /** 竞拍数量小于库存数量，全部成交 */
            if (quantitys > 0 && quantitys <= product.getLaveStock()) {
                creationOrder(product.getId(), product.getShopId(), records);
            }
            /** 竞拍数量大于库存数量，部分成交 */
            else if (quantitys > 0 && quantitys > product.getLaveStock()) {
                creationOrder(product.getId(), product.getShopId(), getSuccessTransaction(product.getLaveStock(), records));
            } else {
                /** 没有用户出价,全部出局 */
                productAuctionUserDao.updateByFail(auction.getProductId());
            }
            productAuctionDao.updateByCreateOrder(auction.getId());
        });
    }

    private int getDownSumQtyByProductId(Long productId, BigDecimal floorPrice, BigDecimal unitPrice) {
        List<ProductAuctionRecord> records = selectDownSuccessList(productId,floorPrice,unitPrice);
        int sumQuantity = 0;
        for (int i = 0; i < records.size(); i++) {
            int quantity = records.get(i).getQuantity().intValue();
            sumQuantity += quantity;
        }
        return sumQuantity;
    }

    private List<ProductAuctionRecord> selectDownSuccessList(Long productId, BigDecimal floorPrice, BigDecimal unitPrice) {
        Set<Long> records = productAuctionRecordDao.selectDownNewUserRecordList(productId);
        if (records.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<ProductAuctionRecord> newRecords =  productAuctionRecordDao.selectDownSuccessList(records,floorPrice,unitPrice);
        return newRecords;
    }

    private List<ProductAuctionRecord> getSuccessTransaction(int stock, List<ProductAuctionRecord> records) {
        List<ProductAuctionRecord> newRecords = new ArrayList<>();
        int sumQuantity = 0;
        for (int i = 0; i < records.size(); i++) {
            int currentQty = records.get(i).getQuantity();
            sumQuantity += currentQty;
            if (sumQuantity < stock) {
                newRecords.add(records.get(i));
            } else if (sumQuantity == stock) {
                newRecords.add(records.get(i));
                break;
            } else {
                newRecords.add(records.get(i).setQuantity(stock - (sumQuantity - currentQty)));
                break;
            }
        }
        return newRecords;
    }

    private void creationOrder(Long productId, Long shopId, List<ProductAuctionRecord> records) {
        Set<Long> userIds = records.stream().map(ProductAuctionRecord::getUserId).collect(Collectors.toSet());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("userId", userIds);
        wrapper.eq("productId", productId);
        List<ProductAuctionUser> auctionUser = productAuctionUserDao.selectList(wrapper);
        Map<Long, Long> addressIds = auctionUser.stream().collect(Collectors.toMap(ProductAuctionUser::getUserId, ProductAuctionUser::getAddressId));
        records.forEach(record -> {
            BuyerAuctionOrder buyerAuctionOrder = new BuyerAuctionOrder();
            buyerAuctionOrder.setUserId(record.getUserId()).setUnitPrice(record.getPrice()).setTxnId(record.getTxnId()).setProductId(record.getProductId()).
                    setShopId(shopId).setAddressId(addressIds.get(record.getUserId())).
                    setQuantity(record.getQuantity()).setPayType((byte) 1).setSettleType((byte) 2).setSplitOrder((byte) 1);
            Boolean flag = false;
            try {
                flag = orderRemoteService.createAuctionOrder(buyerAuctionOrder);
            } catch (Exception e) {
                logger.info("生成订单失败[{}]",buyerAuctionOrder.getTxnId());
                productAuctionRecordDao.updateByErrorOrder(record.getId());
            }
            if (flag) {
                logger.info("生成订单成功[{}]", buyerAuctionOrder.getTxnId());
                productAuctionRecordDao.updateByCreateOrder(record.getId());
            }
        });
        int cou = productAuctionUserDao.updateBySuccess(productId, userIds);
        if (cou > 0) {
            productAuctionUserDao.updateByFail(productId);
        }
    }

}
