package com.dtlonline.shop.service.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.order.command.BuyerAuctionOrder;
import com.dtlonline.api.order.remote.OrderRemoteService;
import com.dtlonline.shop.constant.auction.AuctionApplyStatus;
import com.dtlonline.shop.constant.auction.AuctionType;
import com.dtlonline.shop.exception.AuctionException;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionRecordDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionUserDao;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.view.ViewCode;
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
     * @Description 定时任务产生向上竞拍订单 5分钟执行一次
     * @Date 10:11 2019/3/11
     **/
    @Scheduled(cron = "0 0/5 * * * ?")
    public void taskCreationOrder() {
        logger.info("定时任务竞拍生成订单 begin......");
        List<ProductAuction> auctions = productAuctionDao.selectByOver();
        auctions.forEach(auction -> {
            logger.info("[{}]定时任务[{}]生成竞拍订单{}", new Date(), auction.getProductId(),auction);
            Product product = productDao.selectById(auction.getProductId());
            if(AuctionType.DOWN.getCode().equals(auction.getAuctionType())){
                productAuctionDao.updateByCreateOrder(auction.getId());
                productAuctionUserDao.updateByFail(auction.getProductId());
                return;
            }
            int quantitys = getUpSumQtyByProductId(auction.getProductId(),product.getUnitPrice());
            List<ProductAuctionRecord> records = selectUpSuccessList(auction.getProductId(),product.getUnitPrice());
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

    private int getUpSumQtyByProductId(Long productId,BigDecimal unitPrice) {
        List<ProductAuctionRecord> records = selectUpSuccessList(productId,unitPrice);
        int sumQuantity = 0;
        for (int i = 0; i < records.size(); i++) {
            int quantity = records.get(i).getQuantity().intValue();
            sumQuantity += quantity;
        }
        return sumQuantity;
    }

    private List<ProductAuctionRecord> selectUpSuccessList(Long productId, BigDecimal unitPrice) {
        Set<Long> records = productAuctionRecordDao.selectUpNewUserRecordList(productId);
        if (records.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<ProductAuctionRecord> newRecords =  productAuctionRecordDao.selectUpSuccessList(records,unitPrice);
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
                    setQuantity(record.getQuantity()).setPayType((byte) 1).setSettleType((byte) 2).setSplitOrder((byte) 1).setSplitPay((byte)1);
            Boolean flag = false;
            try {
                flag = orderRemoteService.createAuctionOrder(buyerAuctionOrder);
            } catch (Exception e) {
                logger.info("生成向上竞拍订单失败[{}]",buyerAuctionOrder.getTxnId());
                productAuctionRecordDao.updateByErrorOrder(record.getId());
            }
            if (flag) {
                logger.info("生成向上竞拍订单成功[{}]", buyerAuctionOrder.getTxnId());
                productAuctionRecordDao.updateByCreateOrder(record.getId());
            }
        });
        int cou = productAuctionUserDao.updateBySuccess(productId, userIds);
        if (cou > 0) {
            productAuctionUserDao.updateByFail(productId);
        }
    }

    /**
     * @Author gaoqiang
     * @Description 用于手动重新生成竞拍订单
     * @Date 13:54 2019/5/20
     * @Param recordId  竞拍记录Id
     * @return
     **/
    public void rebuildExceptionOrder(Long recordId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", recordId);
        wrapper.eq("buildOrder",3);
        ProductAuctionRecord record = productAuctionRecordDao.selectOne(wrapper);
        Optional.ofNullable(record).orElseThrow(() -> new AuctionException(ViewCode.FAILURE.getCode(), "竞拍记录信息异常"));
        Product product = productDao.selectById(record.getProductId());
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("userId", record.getUserId());
        userWrapper.eq("productId", product.getId());
        ProductAuctionUser auctionUser = productAuctionUserDao.selectOne(userWrapper);
        BuyerAuctionOrder buyerAuctionOrder = new BuyerAuctionOrder();
        buyerAuctionOrder.setUserId(record.getUserId()).setUnitPrice(record.getPrice()).setTxnId(record.getTxnId()).setProductId(record.getProductId()).
                setShopId(product.getShopId()).setAddressId(auctionUser.getAddressId()).
                setQuantity(record.getQuantity()).setPayType((byte) 1).setSettleType((byte) 2).setSplitOrder((byte) 1).setSplitPay((byte)1);
        Boolean flag = false;
        try {
            flag =  orderRemoteService.createAuctionOrder(buyerAuctionOrder);
        } catch (Exception e) {
            logger.info("生成向上订单失败[{}]", buyerAuctionOrder.getTxnId());
            throw new AuctionException(ViewCode.FAILURE.getCode(), "生成向上订单失败");
        }
        if (flag) {
            logger.info("生成向上竞拍订单成功[{}]", buyerAuctionOrder.getTxnId());
            productAuctionRecordDao.updateByCreateOrderByRebuild(record.getId());
        }

    }

}
