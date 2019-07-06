package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.*;
import com.dtlonline.api.shop.constant.ProductTypeEnum;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.*;
import com.dtlonline.shop.mapper.auction.ProductAuctionDao;
import com.dtlonline.shop.model.*;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.view.*;
import com.google.common.collect.Maps;
import io.alpha.app.core.base.BaseService;
import io.alpha.security.aes.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductRecordService extends BaseService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductRecordDao productRecordDao;

    @Autowired
    private ProductStandardRecordDao productStandardRecordDao;

    @Autowired
    private ProductAuctionDao productAuctionDao;

    @Autowired
    private ProductCarInfoDao productCarInfoDao;

    @Autowired
    private ProductPositionDao productPositionDao;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    public Page queryProductRecordList(Integer current,Integer size,String title,Integer status) {
        Page page = new Page(current,size);
        Map<String, Object> condition = Maps.newHashMap();
        condition.put("title", title);
        condition.put("status", status);
        List<ProductRecord> productList = productRecordDao.productRecordList(page, condition);
        if (productList.isEmpty()) {
            return page;
        }
        page.setRecords(productRecordOtherExtra(productList));
        return page;
    }

    public List<ProductRecordListDTO> productRecordOtherExtra(List<ProductRecord> productList) {
        //查询一组图片地址
        Set<Long> urlIds = productList.stream().map(ProductRecord::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(urlIds);

        //查询一组商品
        Set<Long> shopIds = productList.stream().map(ProductRecord::getShopId).collect(Collectors.toSet());
        Map<Long, ShopDTO> shopMap = shopService.queryByIds(shopIds);

        //组合结果
        List<ProductRecordListDTO> productListDTOS = new ArrayList(productList.size());
        productList.forEach(record -> {
            ShopDTO shop = shopMap.get(record.getShopId());
            Set<Long> imgIds = Arrays.stream(record.getImgs().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<String> imageList = new ArrayList(imgIds.size());
            imgIds.forEach(id -> imageList.add(urls.get(id)));
            productListDTOS.add(ProductRecordListDTO.of(record, shop, imageList));
        });
        return productListDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProductOtherExtra(ProductRecordDTO productRecordDTO, Long userId) {
        ProductRecord productRecord = new ProductRecord();
        BeanUtils.copyProperties(productRecordDTO, productRecord);
        Boolean isType = TypeEnum.PRODUCT_PURCHASE.getValue().equals(productRecord.getType());
        if (isType) {
            productRecord.setShopId(0L);
        }
        if (CollectionUtils.isNotEmpty(productRecordDTO.getImageList())) {
            List<Long> imgs = imageRemoteService.batchSaveImage(productRecordDTO.getImageList());
            productRecord.setImgs(StringUtils.strip(imgs.toString(), "[]").replace(" ", ""));
        } else {
            productRecord.setImgs(categoryService.queryCategorys(productRecord.getCategoryId()).getImgs());
        }
        productRecord.setStatus(ProductTypeEnum.PENDING.getValue()).setUserId(userId);
        productRecordDao.insert(productRecord);
        //保存规格
        issueStandard(productRecordDTO,userId);
        //竞拍信息
        if (TypeEnum.PRODUCT_AUCTION.getValue().equals(productRecordDTO.getType())) {
            ProductAuction productAuction = new ProductAuction();
            BeanUtils.copyProperties(productRecordDTO.getAuctionApply(), productAuction);
            productAuction.setTxnId(productRecord.getTxnId()).setProductId(productRecord.getId()).setUserId(userId)
                    .setTitle(productRecord.getTitle())
                    .setBeginTime(productRecordDTO.getAuctionApply().getBeginTime())
                    .setEndTime(productRecordDTO.getAuctionApply().getBeginTime().plusHours(productRecordDTO.getAuctionApply().getHours()));
            productAuctionDao.insert(productAuction);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void issueStandard(ProductRecordDTO productRecordDTO,Long userId){
        if (CollectionUtils.isNotEmpty(productRecordDTO.getStandardList())) {
            productRecordDTO.getStandardList().stream().forEach(standard -> {
                ProductStandardRecord productStandardRecord = new ProductStandardRecord()
                        .setProductName(productRecordDTO.getTitle()).setTxnId(productRecordDTO.getTxnId())
                        .setStandardName(standard.getStandardName()).setStandardId(standard.getStandardId())
                        .setCategoryId(productRecordDTO.getCategoryId()).setCategoryName(productRecordDTO.getCategoryName())
                        .setData(standard.getData()).setStatus(SUCCESS.intValue()).setUserId(userId);
                productStandardRecordDao.insert(productStandardRecord);
            });
        } else {
            ProductStandardRecord productStandardRecord = new ProductStandardRecord()
                    .setProductName(productRecordDTO.getTitle())
                    .setTxnId(productRecordDTO.getTxnId())
                    .setCategoryId(productRecordDTO.getCategoryId())
                    .setCategoryName(productRecordDTO.getCategoryName())
                    .setStatus(SUCCESS.intValue()).setUserId(userId);
            productStandardRecordDao.insert(productStandardRecord);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void issueProductRecord(ProductRecordParamDTO productRecordParam,Long userId){
        ProductRecord productRecord = new ProductRecord();
        BeanUtils.copyProperties(productRecordParam, productRecord);
        productRecord.setImgs(categoryService.queryCategorys(productRecord.getCategoryId()).getImgs());
        productRecord.setStatus(ProductTypeEnum.PENDING.getValue()).setUserId(userId);
        productRecordDao.insert(productRecord);
        productRecordParam.getStandardList().stream().forEach(standard -> {
            ProductStandardRecord productStandardRecord = new ProductStandardRecord()
                    .setProductName(productRecordParam.getTitle()).setTxnId(productRecordParam.getTxnId())
                    .setStandardName(standard.getStandardName()).setStandardId(standard.getStandardId())
                    .setCategoryId(productRecordParam.getCategoryId()).setCategoryName(productRecordParam.getCategoryName())
                    .setData(standard.getData()).setStatus(SUCCESS.intValue()).setUserId(userId);
            productStandardRecordDao.insert(productStandardRecord);
        });
        ProductPosition position = new ProductPosition();
        BeanUtils.copyProperties(productRecordParam.getPosition(),position);
        position.setTxnId(productRecordParam.getTxnId());
        productPositionDao.insert(position);
        if (TypeEnum.PRODUCT_SUPPLY.getValue().equals(productRecordParam.getType()) && productRecordParam.getTheWay() == 2){
            ProductCarInfo productCarInfo = new ProductCarInfo();
            BeanUtils.copyProperties(productRecordParam.getProductCar(),productCarInfo);
            productCarInfo.setTxnId(productRecordParam.getTxnId());
            productCarInfoDao.insert(productCarInfo);
        }
    }

    public ProductRecordDetailDTO queryProductRecordDetail(Long id) {
        ProductRecord productRecord = productRecordDao.selectById(id);
        if (productRecord == null) {
            Optional.ofNullable(productRecord).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), ViewCode.PRODUCT_FAILURE.getMessage()));
            return null;
        }
        ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(productRecord.getTxnId())
                .setCategoryId(productRecord.getCategoryId());
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardRecordListDTO> productStandardRecords = new ArrayList<>(standardList.size());
        standardList.forEach(addr -> productStandardRecords.add(ProductStandardRecordListDTO.of(addr)));
        ProductRecordDetailDTO productRecordDetailDTO = ProductRecordDetailDTO.of(productRecord);
        productRecordDetailDTO.setProductStandardList(productStandardRecords)
                .setImgList(queryImgsOtherExtra(productRecord.getImgs()))
                .setCategory(categoryService.queryCategorys(productRecord.getCategoryId()))
                .setLabel(Status.getStatusType(productRecordDetailDTO.getStatus()).getMeaning());
        return productRecordDetailDTO;
    }

    public List<String> queryImgsOtherExtra(String imgs) {
        Set<Long> imgIds = Arrays.stream(imgs.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(imgIds);
        List<String> imageList = new ArrayList(urls.size());
        imgIds.forEach(id -> imageList.add(urls.get(id)));
        return imageList;
    }

    public void approval(String recordName, CheckInfoDTO checkInfoDTO) {
        logger.info("[{}] 进行商品审核 [{}]", recordName, checkInfoDTO.getOpinion());
        Integer result = productRecordDao.updateProductByStatus(AESUtil.encrypt(recordName), checkInfoDTO);
        ProductRecord productRecord = productRecordDao.selectById(checkInfoDTO.getId());
        Product product = new Product();
        if (Status.PASS.getValue().equals(checkInfoDTO.getStatus()) && Status.PASS.getValue().equals(result)) {
            BeanUtils.copyProperties(productRecord, product);
            product.setStatus(ProductTypeEnum.RACK.getValue());
            product.setLaveStock(productRecord.getStock());
            productDao.insert(product);
        }
        if (TypeEnum.PRODUCT_AUCTION.getValue().equals(productRecord.getType())) {
            Integer count = productAuctionDao.updateAuctionByStatus(AESUtil.encrypt(recordName), productRecord.getTxnId(), product.getId(), checkInfoDTO);
            Optional.ofNullable(count).filter(c -> !c.equals(0)).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), "已审核的竞拍商品不能再审核"));
        }
        if (!Status.PASS.getValue().equals(result)) {
            throw new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), "已审核的商品不能再审核");
        }
    }

    public Integer rack(Long id, Long userId) {
        Integer result = productDao.updateRackById(id, ProductTypeEnum.RACK.getValue(), userId);
        if (result == 0) {
            throw new ShopException(ViewCode.SHOP_RACK_FAILURE.getCode(), ViewCode.SHOP_RACK_FAILURE.getMessage());
        }
        return result;
    }

    public Integer undoRack(Long id, Long userId) {
        Integer result = productDao.updateUnRackById(id, ProductTypeEnum.UN_RACK.getValue(), userId);
        if (result == 0) {
            throw new ShopException(ViewCode.SHOP_UN_RACK_FAILURE.getCode(), ViewCode.SHOP_UN_RACK_FAILURE.getMessage());
        }
        return result;
    }

    public IPage<ProductBriefnessDTO> queryItsRecordsExceptPassProducts(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO, Long userId) {
        Optional.ofNullable(userId).orElseThrow(() -> new ShopException(ViewCode.SHOP_UN_RACK_LIST_FAILURE.getCode(), "请先登录再查看待审核的商品"));
        IPage<ProductBriefnessDTO> productBriefnessDTOIPage =
                productRecordDao.queryItsRecordsExceptPassProducts(shopWithProductQueryPageDTO, userId);
        productBriefnessDTOIPage.getRecords().forEach(x -> {
            Long mainImageString = Long.valueOf(x.getImgs().split(",")[0]);
            x.setImgs(Optional.ofNullable(imageRemoteService.queryUrl(mainImageString)).orElse(""));
            // TODO: 2019/3/15 魔法数字 5 和 4
            Integer status = x.getStatus().equals(Status.PENDING.getValue()) ? 5 : 4;
            x.setStatus(status);
        });
        return productBriefnessDTOIPage;
    }
}
