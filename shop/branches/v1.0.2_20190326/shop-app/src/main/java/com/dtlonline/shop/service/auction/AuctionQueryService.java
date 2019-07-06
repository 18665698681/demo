package com.dtlonline.shop.service.auction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.user.remote.InfosRemoteService;
import com.dtlonline.shop.constant.auction.AuctionApplyStatus;
import com.dtlonline.shop.constant.auction.AuctionAuditStatus;
import com.dtlonline.shop.constant.auction.AuctionProcessStatus;
import com.dtlonline.shop.constant.auction.AuctionUserStatus;
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
import com.dtlonline.shop.service.ProductService;
import com.dtlonline.shop.view.CategoryDTO;
import com.dtlonline.shop.view.ProductDetailDTO;
import com.dtlonline.shop.view.ProductStandardRecordListDTO;
import com.dtlonline.shop.view.auction.ProductAuctionDTO;
import com.dtlonline.shop.view.auction.ProductAuctionDetailDTO;
import com.dtlonline.shop.view.auction.ProductAuctionRecordDTO;
import com.dtlonline.shop.view.auction.ProductAuctionUserDTO;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.MaskUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuctionQueryService extends BaseService implements AuctionApplyStatus {

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRecordService productRecordService;

    @Autowired
    private ProductAuctionUserDao productAuctionUserDao;

    @Autowired
    private ProductAuctionRecordDao productAuctionRecordDao;

    @Autowired
    private InfosRemoteService infosRemoteService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductStandardRecordDao productStandardRecordDao;

    @Autowired
    private ProductRecordDao productRecordDao;

    @Autowired
    private ImageRemoteService imageRemoteService;

    /**
     * @param process 1未开始 2竞拍中 3已结束
     * @Author gaoqiang
     * @Description 查询平台竞拍活动
     * @Date 10:56 2019/3/9
     **/
    public Object queryAuctionListByPlatform(Integer current, Integer size, Integer process) {
        Page page = new Page(current, size);
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        LocalDateTime date = LocalDateTime.now();
        if (AuctionProcessStatus.BEFORE.getCode().equals(process)) {
            wrapper.orderByAsc("beginTime");
            wrapper.gt("beginTime", date);
        }
        if (AuctionProcessStatus.GOINGON.getCode().equals(process)) {
            wrapper.orderByAsc("endTime");
            wrapper.le("beginTime", date);
            wrapper.ge("endTime", date);
        }
        if (AuctionProcessStatus.OVER.getCode().equals(process)) {
            wrapper.orderByDesc("endTime");
            wrapper.lt("endTime", date);
        }
        productAuctionDao.selectPage(page, wrapper);
        List<ProductAuction> auctions = page.getRecords();
        return page.setRecords(getProductAuctionDTOList(auctions));
    }

    /**
     * @param process 1未开始 2竞拍中
     * @Author gaoqiang
     * @Description 查询平台置顶的竞拍活动
     * @Date 15:47 2019/3/13
     **/
    public Object queryAuctionTopByPlatform(Integer process) {
        if (AuctionProcessStatus.OVER.getCode().equals(process)) {
            return null;
        }
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        LocalDateTime date = LocalDateTime.now();
        if (AuctionProcessStatus.BEFORE.getCode().equals(process)) {
            wrapper.orderByAsc("beginTime");
            wrapper.gt("beginTime", date);
        }
        if (AuctionProcessStatus.GOINGON.getCode().equals(process)) {
            wrapper.orderByAsc("endTime");
            wrapper.le("beginTime", date);
            wrapper.ge("endTime", date);
        }
        wrapper.last(" limit 1 ");
        List<ProductAuction> auctions = productAuctionDao.selectList(wrapper);
        return getProductAuctionDTOList(auctions);
    }

    public Object queryAuctionListByHome() {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        LocalDateTime date = LocalDateTime.now();
        wrapper.orderByAsc("beginTime");
        wrapper.ge("endTime", date);
        wrapper.last(" limit 8 ");
        List<ProductAuction> auctions = productAuctionDao.selectList(wrapper);
        return getProductAuctionDTOList(auctions);
    }

    /**
     * @Author gaoqiang
     * @Description 卖家发布的竞拍列表
     * @Date 10:30 2019/3/8
     **/
    public Object queryAuctionListBySeller(Long userId, Integer current, Integer size) {
        Page page = new Page(current, size);
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.eq("userId", userId);
        productAuctionDao.selectPage(page, wrapper);
        List<ProductAuction> auctions = page.getRecords();
        Set<String> txnIds = auctions.stream().map(ProductAuction::getTxnId).collect(Collectors.toSet());
        QueryWrapper<ProductRecord> wrapperRecord = new QueryWrapper<>();
        wrapperRecord.in("txnId", txnIds);
        List<ProductRecord> records = productRecordDao.selectList(wrapperRecord);
        Map<String, ProductInfoDTO> products = getProductInfoDTO(records);
        Collection<ProductInfoDTO> sss = products.values();
        Set<Long> categoryIds = sss.stream().map(ProductInfoDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryDTOs = categoryService.queryCategoryMap(categoryIds);
        List<ProductAuctionDTO> DTOS = auctions.stream().map(d -> ProductAuctionDTO.of(d, products.get(d.getTxnId()), categoryDTOs)).collect(Collectors.toList());
        return page.setRecords(DTOS);
    }

    /**
     * @param process -1参与的竞拍 1竞拍中 2竞拍成功 3竞拍结束
     * @Author gaoqiang
     * @Description 查询用户参与的竞拍活动
     * @Date 10:56 2019/3/9
     **/
    public Object queryAuctionListByUser(Long userId, Integer current, Integer size, Integer process) {
        Page page = new Page(current, size);
        QueryWrapper<ProductAuctionUser> wrapperUser = new QueryWrapper<>();
        wrapperUser.orderByDesc("id");
        wrapperUser.eq("userId", userId);
        LocalDateTime date = LocalDateTime.now();
        if (AuctionUserStatus.GOINGON.getCode().equals(process)) {
            wrapperUser.le("beginTime", date);
            wrapperUser.ge("endTime", date);
            wrapperUser.eq("isSuccess", 1);
            wrapperUser.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        }
        if (AuctionUserStatus.SUCCESS.getCode().equals(process)) {
            wrapperUser.eq("isSuccess", 2);
            wrapperUser.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        }
        if (AuctionUserStatus.OVER.getCode().equals(process)) {
            wrapperUser.lt("endTime", date);
            wrapperUser.eq("auditStatus", AuctionAuditStatus.PASS.getCode());
        }
        productAuctionUserDao.selectPage(page, wrapperUser);
        List<ProductAuctionUser> auctionUsers = page.getRecords();
        return page.setRecords(getProductAuctionUserDTOList(auctionUsers, userId));
    }

    /**
     * @Author gaoqiang
     * @Description 查询竞拍活动详情
     * @Date 10:57 2019/3/9
     **/
    public Object queryAuctionDetails(Long userId, Long productId) {
        ProductAuctionDetailDTO dao = querySellerDetail(productId);
        dao.setIsApply(getApplyStatus(productId, userId));
        dao.setUserAuditStatus(getUserAduitStatus(productId,userId));
        dao.setRecordDtos(getLastRecords(productId));
        dao.setCurrentPrice(dao.getRecordDtos().isEmpty() ? dao.getProduct().getUnitPrice() : dao.getRecordDtos().get(0).getPrice());
        return dao;
    }

    /**
     * @Author gaoqiang
     * @Description 卖家查询详情
     * @Date 10:30 2019/3/8
     **/
    public ProductAuctionDetailDTO querySellerDetail(Long productId) {
        QueryWrapper<ProductAuction> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productId);
        ProductAuction productAuction = productAuctionDao.selectOne(wrapper);
        ProductDetailDTO productDetailDTO = getProductDetailDTOByTxnId(productAuction.getTxnId());
        CategoryDTO categoryDTO = categoryService.queryCategorys(productDetailDTO.getCategoryId());
        productDetailDTO.setProductStandardList(getStandardList(productAuction.getTxnId(), categoryDTO.getId()));
        productDetailDTO.setCategory(categoryDTO);
        ProductAuctionDetailDTO dao = ProductAuctionDetailDTO.of(productAuction);
        dao.setProduct(productDetailDTO);
        return dao;
    }

    /**
     * @Author gaoqiang
     * @Description 查询出价记录
     * @Date 10:57 2019/3/9
     **/
    public Object queryAuctionRecords(Integer current, Integer size, Long productId) {
        Page page = new Page(current, size);
        QueryWrapper<ProductAuctionRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productId);
        wrapper.orderByDesc("price", "quantity");
        wrapper.orderByAsc("createTime");
        productAuctionRecordDao.selectPage(page, wrapper);
        List<ProductAuctionRecordDTO> dtos = new ArrayList<>();
        List<ProductAuctionRecord> records = page.getRecords();
        records.stream().filter(s -> s != null).forEach(record -> {
            dtos.add(ProductAuctionRecordDTO.of(record).setAccount(MaskUtils.maskMobile(infosRemoteService.queryUserAuthDetail(record.getUserId()).getMobile())));
        });
        return page.setRecords(dtos);
    }

    /**
     * @Author gaoqiang
     * @Description 当前用户出价的最高价
     * @Date 10:14 2019/3/9
     **/
    public Object queryUserMaxPrice(Long userId, Long productId) {
        ProductAuctionRecord userMaxPrice = productAuctionRecordDao.selectUserMaxPrice(userId, productId);
        Map<String, Object> resultMap = new HashMap<>();
        if (userMaxPrice == null) {
            Product product = productDao.selectById(productId);
            resultMap.put("price", product.getUnitPrice());
            resultMap.put("quantity", product.getMinDeal());
        } else {
            resultMap.put("price", userMaxPrice.getPrice());
            resultMap.put("quantity", userMaxPrice.getQuantity());
        }
        return resultMap;
    }

    private Map<String, ProductInfoDTO> getProductInfoDTO(List<ProductRecord> records) {
        Map<String, ProductInfoDTO> dtos = new HashMap<>();
        Set<Long> urlIds = records.stream().map(ProductRecord::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(urlIds);
        records.forEach(r -> {
            ProductInfoDTO infoDTO = new ProductInfoDTO();
            BeanUtils.copyProperties(r, infoDTO);
            Set<Long> productUrlIds = Arrays.stream(infoDTO.getImgs().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<String> imageList = new ArrayList<>(productUrlIds.size());
            productUrlIds.forEach(id -> imageList.add(urls.get(id)));
            infoDTO.setImgs(imageList.isEmpty() ? "" : imageList.get(0));
            infoDTO.setLaveStock(r.getStock());
            dtos.put(r.getTxnId(), infoDTO);
        });
        return dtos;
    }

    public Integer getUserAduitStatus(Long productId, Long userId) {
        if (userId == 0L) {
            return AuctionAuditStatus.NOT_PASS.getCode();
        }
        QueryWrapper<ProductAuctionUser> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productId);
        wrapper.eq("userId", userId);
        ProductAuctionUser user = productAuctionUserDao.selectOne(wrapper);
        if (user == null) {
            return AuctionAuditStatus.NOT_PASS.getCode();
        }else{
            return user.getAuditStatus();
        }
    }

    public Integer getApplyStatus(Long productId, Long userId) {
        if (userId == 0L) {
            return NO_APPLY;
        }
        QueryWrapper<ProductAuctionUser> wrapper = new QueryWrapper<>();
        wrapper.eq("productId", productId);
        wrapper.eq("userId", userId);
        int quantity = productAuctionUserDao.selectCount(wrapper);
        if (quantity > 0) {
            return APPLY;
        } else {
            return NO_APPLY;
        }
    }

    private List<ProductAuctionRecordDTO> getLastRecords(Long productId) {
        List<ProductAuctionRecordDTO> dtos = new ArrayList<>();
        List<ProductAuctionRecord> records = productAuctionRecordDao.selectLastRecords(productId);
        records.stream().filter(s -> s != null).forEach(record -> {
            dtos.add(ProductAuctionRecordDTO.of(record).setAccount(MaskUtils.maskMobile(infosRemoteService.queryUserAuthDetail(record.getUserId()).getMobile())));
        });
        return dtos;
    }

    private List<ProductAuctionDTO> getProductAuctionDTOList(List<ProductAuction> auctions) {
        if (auctions.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Set<Long> productIds = auctions.stream().map(ProductAuction::getProductId).collect(Collectors.toSet());
        Map<Long, ProductInfoDTO> products = productService.queryProductInfoByIds(productIds);
        Collection<ProductInfoDTO> sss = products.values();
        Set<Long> categoryIds = sss.stream().map(ProductInfoDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryDTOs = categoryService.queryCategoryMap(categoryIds);
        return auctions.stream().map(d -> ProductAuctionDTO.of(d, products.get(d.getProductId()), categoryDTOs)).collect(Collectors.toList());
    }


    List<ProductStandardRecordListDTO> getStandardList(String txnId, Long categoryId) {
        ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(txnId).setCategoryId(categoryId);
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardRecordListDTO> productStandardRecords = new ArrayList<>(standardList.size());
        standardList.forEach(addr -> productStandardRecords.add(ProductStandardRecordListDTO.of(addr)));
        return productStandardRecords;
    }

    private ProductDetailDTO getProductDetailDTOByTxnId(String txnId) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        QueryWrapper<ProductRecord> wrapperRecord = new QueryWrapper<>();
        wrapperRecord.eq("txnId", txnId);
        ProductRecord productRecord = productRecordDao.selectOne(wrapperRecord);

        BeanUtils.copyProperties(productRecord, productDetailDTO);
        productDetailDTO.setStock(productRecord.getStock());
        productDetailDTO.setLaveStock(productRecord.getStock());
        productDetailDTO.setImgList(productRecordService.queryImgsOtherExtra(productRecord.getImgs()));

        return productDetailDTO;
    }

    private List<ProductAuctionUserDTO> getProductAuctionUserDTOList(List<ProductAuctionUser> auctionUsers, Long userId) {
        if (auctionUsers.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Set<Long> productIds = auctionUsers.stream().map(ProductAuctionUser::getProductId).collect(Collectors.toSet());
        Map<Long, ProductAuction> auctions = queryAuctionByProductIds(productIds);
        Map<Long, ProductInfoDTO> products = productService.queryProductInfoByIds(productIds);
        Map<Long, BigDecimal> currenMaxPrices = getCurrenMaxPrices(productIds);
        Map<Long, BigDecimal> userPrices = getUserPrices(productIds, userId);
        Collection<ProductInfoDTO> sss = products.values();
        Set<Long> categoryIds = sss.stream().map(ProductInfoDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryDTOs = categoryService.queryCategoryMap(categoryIds);
        return auctionUsers.stream().map(d -> ProductAuctionUserDTO.of(products.get(d.getProductId()), auctions.get(d.getProductId()),d, currenMaxPrices.get(d.getProductId()), userPrices.get(d.getProductId()), categoryDTOs)).collect(Collectors.toList());
    }

    private Map<Long, BigDecimal> getUserPrices(Set<Long> productIds, Long userId) {
        List<ProductAuctionRecord> currenMaxPrices = productAuctionRecordDao.selectUserPriceByProductId(productIds, userId);
        Map<Long, BigDecimal> map = currenMaxPrices.stream().filter(s -> s != null).collect(Collectors.toMap(ProductAuctionRecord::getProductId, ProductAuctionRecord::getPrice));
        return map;
    }

    private Map<Long, BigDecimal> getCurrenMaxPrices(Set<Long> productIds) {
        List<ProductAuctionRecord> currenMaxPrices = productAuctionRecordDao.selectMaxPriceByProductId(productIds);
        Map<Long, BigDecimal> map = currenMaxPrices.stream().filter(s -> s != null).collect(Collectors.toMap(ProductAuctionRecord::getProductId, ProductAuctionRecord::getPrice));
        return map;
    }

    private Map<Long, ProductAuction> queryAuctionByProductIds(Set<Long> productIds) {

        QueryWrapper<ProductAuction> wrapper = new QueryWrapper();
        wrapper.in("productId", productIds);
        List<ProductAuction> list = productAuctionDao.selectList(wrapper);

        Map<Long, ProductAuction> auctions = list.stream().collect(Collectors.toMap(ProductAuction::getProductId, Function.identity()));
        return auctions;

    }


}
