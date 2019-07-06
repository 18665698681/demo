package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.ShopWithProductQueryPageDTO;
import com.dtlonline.api.shop.constant.ProductTypeEnum;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.ProductRecordDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.ProductRecord;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.view.*;
import com.google.common.collect.Maps;
import io.alpha.core.base.BaseService;
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
    private ImageRemoteService imageRemoteService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    public Page queryProductRecordList(ProductRecordDTO productRecordDTO){
        Page page = productRecordDTO.getMyBatisPage();
        Map<String,Object> condition= Maps.newHashMap();
        condition.put("title",productRecordDTO.getTitle());
        condition.put("status",productRecordDTO.getStatus());
        List<ProductRecord> productList = productRecordDao.productRecordList(page,condition);
        if (productList.isEmpty()){
            return page;
        }
        page.setRecords(productRecordOtherExtra(productList));
        return page;
    }

    public List<ProductRecordListDTO> productRecordOtherExtra(List<ProductRecord> productList){
        //查询一组图片地址
        Set<Long> urlIds = productList.stream().map(ProductRecord::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(urlIds);

        //查询一组商品
        Set<Long> shopIds = productList.stream().map(ProductRecord::getShopId).collect(Collectors.toSet());
        Map<Long, ShopDTO> shopMap= shopService.queryByIds(shopIds);

        //组合结果
        List<ProductRecordListDTO> productListDTOS = new ArrayList(productList.size());
        productList.forEach(record -> {
            ShopDTO shop = shopMap.get(record.getShopId());
            Set<Long> imgIds = Arrays.stream(record.getImgs().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<String> imageList = new ArrayList(imgIds.size());
            imgIds.forEach(id-> imageList.add(urls.get(id)));
            productListDTOS.add(ProductRecordListDTO.of(record,shop,imageList));
        });
        return productListDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProductOtherExtra(ProductRecordDTO productRecordDTO, Long userId){
        // TODO: 2019/3/15 保存商品单独封装一个方法
        ProductRecord productRecord = new ProductRecord();
        BeanUtils.copyProperties(productRecordDTO,productRecord);
        //采购商品不需要关联店铺
        if (null != productRecord.getType() && productRecord.getType() == 99){
            productRecord.setShopId(0L);
        }
        productRecord.setStatus(ProductTypeEnum.PENDING.getValue()).setUserId(userId);
        if (!productRecordDTO.getImageList().isEmpty()){
            List<Long> imgs = imageRemoteService.batchSaveImage(productRecordDTO.getImageList());
            productRecord.setImgs(StringUtils.strip(imgs.toString(),"[]").replace(" ",""));
        }else{
            productRecord.setImgs(categoryService.queryCategorys(productRecord.getCategoryId()).getImgs());
        }
        productRecordDao.insert(productRecord);

        // TODO: 2019/3/15 保存规格单独封装一个方法
        if (!productRecordDTO.getStandardList().isEmpty()){
            //保存品类规格
            productRecordDTO.getStandardList().stream().forEach(standard-> {
                ProductStandardRecord productStandardRecord = new ProductStandardRecord()
                        .setProductName(productRecordDTO.getTitle()).setTxnId(productRecordDTO.getTxnId())
                        .setStandardName(standard.getStandardName()).setStandardId(standard.getStandardId())
                        .setCategoryId(productRecordDTO.getCategoryId()).setCategoryName(productRecordDTO.getCategoryName())
                        .setData(standard.getData()).setStatus(SUCCESS.intValue()).setUserId(userId);
                productStandardRecordDao.insert(productStandardRecord);
            });
        }else{
            ProductStandardRecord productStandardRecord = new ProductStandardRecord()
                    .setProductName(productRecordDTO.getTitle())
                    .setTxnId(productRecordDTO.getTxnId())
                    .setCategoryId(productRecordDTO.getCategoryId())
                    .setCategoryName(productRecordDTO.getCategoryName())
                    .setStatus(SUCCESS.intValue()).setUserId(userId);
            productStandardRecordDao.insert(productStandardRecord);
        }
    }

    public ProductRecordDetailDTO queryProductRecordDetail(Long id){
        ProductRecord productRecord = productRecordDao.selectById(id);
        if (productRecord == null){
            Optional.ofNullable(productRecord).orElseThrow(()->new ProductException(ViewCode.PRODUCT_FAILURE.getCode(),ViewCode.PRODUCT_FAILURE.getMessage()));
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
    public List<String> queryImgsOtherExtra(String imgs){
        Set<Long> imgIds = Arrays.stream(imgs.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long,String> urls = imageRemoteService.queryUrls(imgIds);
        List<String> imageList = new ArrayList(urls.size());
        imgIds.forEach(id->imageList.add(urls.get(id)));
        return imageList;
    }

    public void approval(String recordName,CheckInfoDTO checkInfoDTO){
        logger.info("[{}] 进行商品审核 [{}]",recordName,checkInfoDTO.getOpinion());
        Integer result = productRecordDao.updateProductByStatus(AESUtil.encrypt(recordName),checkInfoDTO);
        if (Status.PASS.getValue().equals(checkInfoDTO.getStatus()) && Status.PASS.getValue().equals(result)){
            ProductRecord productRecord = productRecordDao.selectById(checkInfoDTO.getId());
            Product product = new Product();
            BeanUtils.copyProperties(productRecord,product);
            product.setStatus(ProductTypeEnum.RACK.getValue());
            product.setLaveStock(productRecord.getStock());
            productDao.insert(product);
        }
        if (!Status.PASS.getValue().equals(result)){
            throw new ProductException(ViewCode.PRODUCT_FAILURE.getCode(),"已审核的商品不能再审核");
        }
    }

    public Integer rack(Long id,Long userId){
        Integer result = productDao.updateRackById(id,ProductTypeEnum.RACK.getValue(),userId);
        if (result == 0){
            throw new ShopException(ViewCode.SHOP_RACK_FAILURE.getCode(),ViewCode.SHOP_RACK_FAILURE.getMessage());
        }
        return result;
    }

    public Integer undoRack(Long id,Long userId){
        Integer result = productDao.updateUnRackById(id,ProductTypeEnum.UN_RACK.getValue(),userId);
        if (result == 0){
            throw new ShopException(ViewCode.SHOP_UN_RACK_FAILURE.getCode(),ViewCode.SHOP_UN_RACK_FAILURE.getMessage());
        }
        return result;
    }

    public IPage<ProductBriefnessDTO> queryItsRecordsExceptPassProducts(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO, Long userId) {
        Optional.ofNullable(userId).orElseThrow(()-> new ShopException(ViewCode.SHOP_UN_RACK_LIST_FAILURE.getCode(),"请先登录再查看待审核的商品"));
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
