package com.dtlonline.shop.service.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.order.command.BuyerAuctionOrder;
import com.dtlonline.api.order.remote.OrderRemoteService;
import com.dtlonline.api.shop.command.*;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.user.remote.UserAuthRemoteService;
import com.dtlonline.api.user.remote.UserShippingAddressRemoteService;
import com.dtlonline.api.user.view.ShippingAddressDTO;
import com.dtlonline.shop.constant.RedisKey;
import com.dtlonline.shop.constant.auction.AuctionApplyStatus;
import com.dtlonline.shop.constant.auction.AuctionAuditStatus;
import com.dtlonline.shop.constant.auction.AuctionType;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private UserShippingAddressRemoteService userShippingAddressRemoteService;

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
    private AuctionQueryService auctionQueryService;

    @Autowired
    private OrderRemoteService orderRemoteService;

    @Autowired
    private UserAuthRemoteService userAuthRemoteService;

    @Value("${product.auction.lengthen.minutes:2}")
    private int lengthenMinutes;

    public static final String upAuctionPrice = "up_auction_price_";

    @Autowired
    private RedisTemplate<String,BigDecimal> redisTemplate;


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
        auctionUser.setAuctionUserNo(getAuctionUserNo(into.getProductId()));
        productAuctionUserDao.insert(auctionUser);
    }

    public String getAuctionUserNo(Long productId) {
        String no = "";
        QueryWrapper wrapper = new QueryWrapper();
        while (true) {
            no = String.valueOf(new Random().nextInt(899999) + 100000);
            wrapper.eq("auctionUserNo", no);
            //每个竞拍活动保证不重复就可以
            wrapper.eq("productId", productId);
            ProductAuctionUser user = productAuctionUserDao.selectOne(wrapper);
            if (user == null) {
                break;
            }
        }
        return no;
    }

    private ShippingAddressDTO checkUserAddress(Long addressId) {
        ShippingAddressDTO address = userShippingAddressRemoteService.queryUserShippingAddress(addressId).getData();
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
        if (!userAuthRemoteService.queryRealnameAuthStatus(userId).getData() && !userAuthRemoteService.queryEnterpriseAuthStatus(userId).getData()) {
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
    @Transactional(rollbackFor = Exception.class)
    public void auctionBuyer(Long userId, AuctionUserBuyer buyer) {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", buyer.getProductId());
        ProductAuction productAuction = productAuctionDao.selectOne(wrapper);
        checkProductAuctionBuyer(productAuction);
        checkUserBuyer(productAuction, buyer, userId);
        ProductAuctionRecord record = new ProductAuctionRecord();
        BeanUtils.copyProperties(buyer, record);
        record.setUserId(userId);
        if (AuctionType.UP.getCode().equals(productAuction.getAuctionType())) {
            record.setBuildOrder(1);
            checkPriceSyn(record);
            int result = productAuctionRecordDao.insertRecord(record);
            if (result == 0) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "必须高于最后出价，请重新出价");
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now2 = dtf2.format(now.plusMinutes(lengthenMinutes));
            productAuctionDao.updateEndTime(productAuction.getId(), now2);
            productAuctionUserDao.updateEndTime(productAuction.getProductId(), now2);
            String key = RedisKey.PRODUCT_UP_AUCTION_PRICE.getKey(String.valueOf(record.getProductId()));
            redisTemplate.opsForValue().set(key, record.getPrice(), lengthenMinutes, TimeUnit.MINUTES);
        }
        if (AuctionType.DOWN.getCode().equals(productAuction.getAuctionType())) {
            Product product = productDao.selectById(buyer.getProductId());
            //以当前价格为准
            record.setPrice(product.getUnitPrice());
            //状态为已生成订单
            record.setBuildOrder(2);
            productAuctionRecordDao.insert(record);
            Set<Long> userIds = new HashSet<>();
            userIds.add(userId);
            productAuctionUserDao.updateBySuccess(buyer.getProductId(), userIds);
            //向下竞拍出价就形成订单
            logger.info("定时任务向下竞拍生成订单 begin......");
            creationDownOrder(product.getId(), product.getShopId(), record);
        }

    }

    //处理并发问题
    private void checkPriceSyn(ProductAuctionRecord record) {
        String redisKey = record.getProductId() + "_" + record.getPrice();
        long count = redisTemplate.opsForValue().increment(redisKey, 1);
        if (count == 1) {
            redisTemplate.expire(redisKey, lengthenMinutes, TimeUnit.MINUTES);
        }
        if (count > 1) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "价格已存在，请重新出价");
        }

    }

    public void creationDownOrder(Long productId, Long shopId, ProductAuctionRecord record) {
        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("userId", record.getUserId());
        userWrapper.eq("productId", productId);
        ProductAuctionUser auctionUser = productAuctionUserDao.selectOne(userWrapper);
        BuyerAuctionOrder buyerAuctionOrder = new BuyerAuctionOrder();
        buyerAuctionOrder.setUserId(record.getUserId()).setUnitPrice(record.getPrice()).setTxnId(record.getTxnId()).setProductId(record.getProductId()).
                setShopId(shopId).setAddressId(auctionUser.getAddressId()).
                setQuantity(record.getQuantity()).setPayType((byte) 1).setSettleType((byte) 2).setSplitOrder((byte) 1).setSplitPay((byte) 1);
        try {
            orderRemoteService.createAuctionOrder(buyerAuctionOrder);
        } catch (Exception e) {
            logger.info("生成向下订单失败[{}]", buyerAuctionOrder.getTxnId());
            throw new AuctionException(ViewCode.FAILURE.getCode(), "生成向下订单失败");
        }
    }


    private void checkAuctionUser(Long userId, ProductAuction productAuction) {
        if (APPLY.equals(auctionQueryService.getApplyStatus(productAuction.getProductId(), userId))) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "已参加过");
        }
        if (!userAuthRemoteService.queryRealnameAuthStatus(userId).getData() && !userAuthRemoteService.queryEnterpriseAuthStatus(userId).getData()) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "未实名认证或企业认证");
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
        if (auction.getBuildOrder().equals(2)) {
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
        if (AuctionType.UP.getCode().equals(productAuction.getAuctionType())) {
            upBuyerPriceCheck(productAuction, buyer, userId, product);
        }

        if (product.getMinDeal().compareTo(buyer.getQuantity()) == 1) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "不能小于最小交易量" + product.getMinDeal());
        }

        if (buyer.getQuantity().compareTo(product.getLaveStock()) == 1) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "库存数量不足" + product.getMinDeal());
        }
    }


    private void upBuyerPriceCheck(ProductAuction productAuction, AuctionUserBuyer buyer, Long userId, Product product) {
        ProductAuctionRecord userRecord = productAuctionRecordDao.selectUserNewPrice(product.getId());
        if (userRecord != null) {
             String key = RedisKey.PRODUCT_UP_AUCTION_PRICE.getKey(String.valueOf(userRecord.getProductId()));
             BigDecimal currentPrice = redisTemplate.opsForValue().get(key);
             Optional.ofNullable(currentPrice).filter(price -> buyer.getPrice().compareTo(currentPrice) == 1).orElseThrow(() -> new AuctionException(ViewCode.FAILURE.getCode(), "必须高于最后出价"+currentPrice));
             if (buyer.getPrice().compareTo(userRecord.getPrice()) == -1 || buyer.getPrice().compareTo(userRecord.getPrice()) == 0) {
                throw new AuctionException(ViewCode.FAILURE.getCode(), "必须高于最后出价" + userRecord.getPrice());
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

    public void updateSellerPrice(Long userId, AuctionSellerPrice sellerPrice) {
        Product product = productDao.selectById(sellerPrice.getProductId());
        if (product == null) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "获取商品信息失败");
        }
        if (!userId.equals(product.getUserId())) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "只能修改自己发布的商品");
        }
        if (sellerPrice.getPrice().compareTo(product.getUnitPrice()) == 1 || sellerPrice.getPrice().compareTo(product.getUnitPrice()) == 0) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "必须小于当前价格");
        }
        ProductRecordDTO newProduct = new ProductRecordDTO();
        newProduct.setUnitPrice(sellerPrice.getPrice());
        Integer cou = productDao.updateProductForId(sellerPrice.getProductId(), newProduct, userId);
        if (cou != 1) {
            throw new AuctionException(ViewCode.FAILURE.getCode(), "修改价格失败");
        }

    }
}
