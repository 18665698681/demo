package com.dtlonline.shop.service.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.order.command.BuyerAuctionOrder;
import com.dtlonline.api.order.remote.OrderRemoteService;
import com.dtlonline.api.shop.command.AuctionUserBuyer;
import com.dtlonline.api.shop.command.AuctionUserInto;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.user.remote.AuthRemoteService;
import com.dtlonline.api.user.remote.ShippingAddressRemoteService;
import com.dtlonline.api.user.view.ShippingAddressDTO;
import com.dtlonline.shop.constant.auction.AuctionApplyStatus;
import com.dtlonline.shop.constant.auction.AuctionAuditStatus;
import com.dtlonline.shop.constant.auction.AuctionUserCheckStatus;
import com.dtlonline.shop.exception.AuctionException;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.ProductRecordDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionRecordDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionUserDao;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.ProductRecord;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.auction.ProductAuctionRecord;
import com.dtlonline.shop.model.auction.ProductAuctionUser;
import com.dtlonline.shop.service.CategoryService;
import com.dtlonline.shop.service.ProductRecordService;
import com.dtlonline.shop.service.ShopService;
import com.dtlonline.shop.view.ProductRecordDetailDTO;
import com.dtlonline.shop.view.ProductRecordListDTO;
import com.dtlonline.shop.view.ProductStandardRecordListDTO;
import com.dtlonline.shop.view.auction.ProductAuctionRecordDetailDTO;
import com.dtlonline.shop.view.auction.ProductAuctionRecordListDTO;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.view.ViewCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuctionCenterService extends BaseService implements AuctionApplyStatus {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductAuctionUserDao productAuctionUserDao;

    @Autowired
    private ProductAuctionRecordDao productAuctionRecordDao;

    @Autowired
    private ShippingAddressRemoteService shippingAddressRemoteService;

    @Autowired
    private AuthRemoteService authRemoteService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductRecordDao productRecordDao;

    @Autowired
    private ProductRecordService productRecordService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductStandardRecordDao productStandardRecordDao;

    @Autowired
    private OrderRemoteService orderRemoteService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    /**
     * @Author gaoqiang
     * @Description 用户参与
     * @Date 10:58 2019/3/9
     **/
    public void userInto(Long userId, AuctionUserInto into) {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", into.getProductId());
        ProductAuction productAuction = productAuctionDao.selectOne(wrapper);
        checkUserAddress(into.getAddressId());
        checkProductAuctionInto(productAuction);
        checkAuctionUser(userId, productAuction);
        ProductAuctionUser auctionUser = new ProductAuctionUser();
        BeanUtils.copyProperties(into, auctionUser);
        auctionUser.setUserId(userId).setEarnestMoney(productAuction.getBuyerEarnestMoney()).
                setAuditStatus(AuctionAuditStatus.WAIT_AUDIT.getCode()).
                setBeginTime(productAuction.getBeginTime()).setEndTime(productAuction.getEndTime());
        productAuctionUserDao.insert(auctionUser);
    }

    private ShippingAddressDTO checkUserAddress(Long addressId) {
        ShippingAddressDTO address = shippingAddressRemoteService.queryUserShippingAddress(addressId);
        if (address == null) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "收货地址不存在");
        }
        return address;
    }

    public Object userIntoCheck(Long userId, Long productId) {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productId);
        ProductAuction productAuction = productAuctionDao.selectOne(wrapper);
        checkProductAuctionInto(productAuction);
        Map<String, String> result = new HashMap<>();
        if (!authRemoteService.queryRealnameAuthStatus(userId)) {
            result.put("code", AuctionUserCheckStatus.NO_APPROVE.getCode() + "");
            result.put("message", AuctionUserCheckStatus.valuekOf(AuctionUserCheckStatus.NO_APPROVE.getCode()).getMessage());
        } else if (userId.equals(productAuction.getUserId())) {
            result.put("code", AuctionUserCheckStatus.NO_BUY_PRODUCT.getCode() + "");
            result.put("message", AuctionUserCheckStatus.valuekOf(AuctionUserCheckStatus.NO_BUY_PRODUCT.getCode()).getMessage());
        } else {
            result.put("code", AuctionUserCheckStatus.SUCCESS.getCode() + "");
            result.put("message", AuctionUserCheckStatus.valuekOf(AuctionUserCheckStatus.SUCCESS.getCode()).getMessage());
        }
        return result;
    }

    /**
     * @Author gaoqiang
     * @Description 用户出价
     * @Date 11:33 2019/3/9
     **/
    public void auctionBuyer(Long userId, AuctionUserBuyer buyer) {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", buyer.getProductId());
        ProductAuction productAuction = productAuctionDao.selectOne(wrapper);
        checkProductAuctionBuyer(productAuction);
        checkUserBuyer(productAuction, buyer, userId);
        ProductAuctionRecord record = new ProductAuctionRecord();
        BeanUtils.copyProperties(buyer, record);
        record.setUserId(userId);
        productAuctionRecordDao.insert(record);
    }

    /**
     * @Author gaoqiang
     * @Description 定时任务产生竞拍订单
     * @Date 10:11 2019/3/11
     **/
    @Transactional(rollbackFor = Exception.class)
    public void taskCreationOrder() {
        logger.info("定时任务生成订单 begin......");
        List<ProductAuction> auctions = productAuctionDao.selectByOver();
        auctions.forEach(auction -> {
            logger.info("[{}]定时任务[{}]生成订单", new Date(), auction.getProductId());
            Product product = productDao.selectById(auction.getProductId());
            int quantitys = productAuctionRecordDao.selectSumQtyByProductId(auction.getProductId());
            QueryWrapper<ProductAuctionRecord> wrapper = new QueryWrapper<>();
            wrapper.eq("productId", auction.getProductId());
            wrapper.eq("buildOrder", 1);
            wrapper.orderByDesc("price", "quantity");
            wrapper.orderByAsc("createTime");
            /** 竞拍数量小于库存数量，全部成交 */
            if (quantitys > 0 && quantitys <= product.getLaveStock()) {
                List<ProductAuctionRecord> records = productAuctionRecordDao.selectList(wrapper);
                creationOrder(product.getId(), product.getShopId(), records);
            }
            /** 竞拍数量大于库存数量，部分成交 */
            else if (quantitys > 0 && quantitys > product.getLaveStock()) {
                List<ProductAuctionRecord> records = productAuctionRecordDao.selectList(wrapper);
                creationOrder(product.getId(), product.getShopId(), getSuccessTransaction(product.getLaveStock(), records));
            } else {
                /** 没有用户出价,全部出局 */
                productAuctionUserDao.updateByFail(auction.getProductId());
            }
            productAuctionDao.updateByCreateOrder(auction.getId());

        });
    }

    private void checkAuctionUser(Long userId, ProductAuction productAuction) {
        if (APPLY.equals(auctionQueryService.getApplyStatus(productAuction.getProductId(), userId))) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "已参加过");
        }
        if (!authRemoteService.queryRealnameAuthStatus(userId)) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "未实名认证");
        }
        if (userId.equals(productAuction.getUserId())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "不能参与竞拍自己发布的商品");
        }
    }


    private void checkProductAuctionBuyer(ProductAuction auction) {
        checkProductAuctionInto(auction);
        LocalDateTime date = LocalDateTime.now();
        if (date.isBefore(auction.getBeginTime())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "竞拍活动未开始");
        }
    }

    private void checkProductAuctionInto(ProductAuction auction) {
        if (AuctionAuditStatus.WAIT_AUDIT.getCode().equals(auction.getAuditStatus())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "竞拍活动待审核");
        }
        if (AuctionAuditStatus.NOT_PASS.getCode().equals(auction.getAuditStatus())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "竞拍活动审核未通过");
        }
        LocalDateTime date = LocalDateTime.now();
        if (date.isAfter(auction.getEndTime())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "竞拍活动已结束");
        }
    }

    private void checkUserBuyer(ProductAuction productAuction, AuctionUserBuyer buyer, Long userId) {
        Product product = productDao.selectById(productAuction.getProductId());
        QueryWrapper<ProductAuctionUser> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productAuction.getProductId());
        wrapper.eq("userId", userId);
        ProductAuctionUser auctionUser = productAuctionUserDao.selectOne(wrapper);
        if (auctionUser == null) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "用户未参与");
        } else if (!AuctionAuditStatus.PASS.getCode().equals(auctionUser.getAuditStatus())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "竞拍申请未审核通过");
        }
        if (userId.equals(productAuction.getUserId())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "不能竞拍自己发布的商品");
        }

        ProductAuctionRecord userRecord = productAuctionRecordDao.selectLastOneRecord(productAuction.getProductId(), userId);
        if (userRecord != null) {
            if (buyer.getPrice().compareTo(userRecord.getPrice()) == -1) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "不能低于上一次出价");
            }
            if (buyer.getPrice().compareTo(userRecord.getPrice()) == 0) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "此价格已存在，请重新出价");
            }
            if (productAuction.getMinAddPrice().compareTo(buyer.getPrice().subtract(userRecord.getPrice())) == 1) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "最低加价" + productAuction.getMinAddPrice());
            }
        } else {
            if (buyer.getPrice().compareTo(product.getUnitPrice()) == -1) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "不能低于起拍价");
            }
            if (buyer.getPrice().compareTo(product.getUnitPrice()) == 1) {
                if (productAuction.getMinAddPrice().compareTo(buyer.getPrice().subtract(product.getUnitPrice())) == 1) {
                    throw new AuctionException(ViewCode.FAILURE.getCode(), "最低加价" + productAuction.getMinAddPrice());
                }
            }
        }

        BigDecimal subPrice = buyer.getPrice().subtract(product.getUnitPrice());
        if (subPrice.compareTo(BigDecimal.ZERO) == 1 && subPrice.divideAndRemainder(productAuction.getMinAddPrice())[1].compareTo(BigDecimal.ZERO) != 0) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "加价必须为 " + productAuction.getMinAddPrice() + " 的整数倍");
        }

        if (product.getMinDeal().compareTo(buyer.getQuantity()) == 1) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "不能小于最小交易量" + product.getMinDeal());
        }
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
            logger.info("生成订单[{}]", buyerAuctionOrder.getTxnId());
            Boolean flag = false;
            try {
                flag = orderRemoteService.createAuctionOrder(buyerAuctionOrder);
            } catch (Exception e) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "生成订单失败" + buyerAuctionOrder.getTxnId());
            }
            if (flag) {
                productAuctionRecordDao.updateByCreateOrder(record.getId());
            }
        });
        int cou = productAuctionUserDao.updateBySuccess(productId, userIds);
        if (cou > 0) {
            productAuctionUserDao.updateByFail(productId);
        }
    }

    public Page queryProductAuctionLists(Integer current, Integer size, String title, Integer status) {
        Page page = new Page(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        Optional.ofNullable(title).filter(t -> StringUtils.isNotBlank(title)).ifPresent(t -> wrapper.eq("title", title));
        Optional.ofNullable(status).filter(e -> e != null).ifPresent(e -> wrapper.eq("auditStatus", status));
        wrapper.orderByDesc("createTime");
        productAuctionDao.selectPage(page, wrapper);
        if (page.getRecords().isEmpty()) {
            return page;
        }
        List<ProductAuction> auctionList = page.getRecords();
        Set<String> txnIds = auctionList.stream().map(ProductAuction::getTxnId).collect(Collectors.toSet());
        Map<String, ProductRecordListDTO> productRecordMap = queryProductRecordMaps(txnIds);
        List<ProductAuctionRecordListDTO> auctionRecordList = new ArrayList<>();
        auctionList.stream().forEach(auction -> {
            ProductAuctionRecordListDTO productAuctionRecordListDTO = ProductAuctionRecordListDTO.of(auction);
            productAuctionRecordListDTO.setProduct(productRecordMap.get(auction.getTxnId()));
            auctionRecordList.add(productAuctionRecordListDTO);
        });
        page.setRecords(auctionRecordList);
        return page;
    }

    public Map<String, ProductRecordListDTO> queryProductRecordMaps(Set<String> txnIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("txnId", txnIds);
        queryWrapper.orderByDesc("createTime");
        List<ProductRecord> productRecordList = productRecordDao.selectList(queryWrapper);
        Map<String, ProductRecordListDTO> productRecordMap = productRecordList.stream().collect(Collectors.toMap(ProductRecord::getTxnId, ProductRecordListDTO::of));
        return productRecordMap;
    }

    public ProductAuctionRecordDetailDTO queryProductRecordForId(String txnId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("txnId", txnId);
        ProductAuction productAuction = productAuctionDao.selectOne(queryWrapper);
        ProductRecord productRecord = productRecordDao.selectOne(queryWrapper);
        ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(productRecord.getTxnId()).setCategoryId(productRecord.getCategoryId());
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardRecordListDTO> productStandardRecords = new ArrayList<>(standardList.size());
        standardList.forEach(addr -> productStandardRecords.add(ProductStandardRecordListDTO.of(addr)));
        ProductRecordDetailDTO productRecordDetailDTO = ProductRecordDetailDTO.of(productRecord);
        productRecordDetailDTO.setProductStandardList(productStandardRecords)
                .setImgList(productRecordService.queryImgsOtherExtra(productRecord.getImgs()))
                .setCategory(categoryService.queryCategorys(productRecord.getCategoryId()))
                .setShop(shopService.queryById(productRecord.getShopId()))
                .setLabel(Status.getStatusType(productRecordDetailDTO.getStatus()).getMeaning());
        ProductAuctionRecordDetailDTO auctionRecord = ProductAuctionRecordDetailDTO.of(productAuction);
        auctionRecord.setProductDetail(productRecordDetailDTO);
        return auctionRecord;
    }

}
