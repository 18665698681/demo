package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordDetailRequestDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordRequestDTO;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyPublishEnum;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyRecordEnum;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordDetailDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordDetailListDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordListDTO;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordDetailMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyStandardMapper;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecord;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecordDetail;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import com.dtlonline.shop.service.CategoryService;
import com.dtlonline.shop.service.ProductService;
import com.dtlonline.shop.view.CategoryDTO;
import com.google.common.collect.Lists;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductGroupbuyRecordService extends BaseService {

    @Autowired
    private ProductGroupbuyRecordMapper productGroupbuyRecordMapper;

    @Autowired
    private ProductGroupbuyRecordDetailMapper productGroupbuyRecordDetailMapper;

    @Autowired
    private ProductGroupbuyStandardService productGroupbuyStandardService;

    @Autowired
    private ProductGroupbuyService productGroupbuyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Transactional(rollbackFor = Exception.class)
    public void issueProductGroupbuyRecord(ProductGroupbuyRecordRequestDTO groupbuyRecord,Long userId){
        ProductGroupbuy groupbuy = productGroupbuyService.queryGroupbuy(groupbuyRecord.getProductGroupbuyId());

        ProductGroupbuyRecord productGroupbuyRecord = ProductGroupbuyRecord.builder().build();
        BeanUtils.copyProperties(groupbuyRecord,productGroupbuyRecord);
        Integer totalQuantity = groupbuyRecord.getGroupbuyRecordDetailList()
                .stream()
                .map(d -> d.getQuantity())
                .reduce(0, Integer::sum);
        productGroupbuyRecord.setUserId(userId)
                .setQuantity(totalQuantity)
                .setProductGroupbuyId(groupbuy.getId())
                .setProductId(groupbuy.getProductId())
                .setBalanceType(groupbuy.getBalanceType());
        productGroupbuyRecordMapper.insert(productGroupbuyRecord);
        issueProductGroupbuyRecordDetail(groupbuyRecord.getGroupbuyRecordDetailList(),productGroupbuyRecord,userId);
    }

    public void issueProductGroupbuyRecordDetail(List<ProductGroupbuyRecordDetailRequestDTO> groupbuyRecordDetailList,ProductGroupbuyRecord productGroupbuyRecord,Long userId){
        if (CollectionUtils.isNotEmpty(groupbuyRecordDetailList)){
            Set<Long> categoryIds = groupbuyRecordDetailList
                    .stream()
                    .map(ProductGroupbuyRecordDetailRequestDTO::getCategoryId)
                    .collect(Collectors.toSet());
            Map<Long, CategoryDTO> categoryDTOMap = categoryService.queryCategoryMap(categoryIds);
            groupbuyRecordDetailList.stream().forEach(record ->{
                ProductGroupbuyRecordDetail productGroupbuyRecordDetail = ProductGroupbuyRecordDetail.builder().build();
                BeanUtils.copyProperties(record,productGroupbuyRecordDetail);
                record.getStandardList().sort((s1,s2)->s1.getStandardId().intValue()-s2.getStandardId().intValue());
                List<String> standardList = record.getStandardList()
                        .stream().map(ProductStandardRecordDTO::getData)
                        .collect(Collectors.toList());
                Map<String, String> standardInfo = record.getStandardList()
                        .stream()
                        .collect(Collectors.toMap(ProductStandardRecordDTO::getStandardName, ProductStandardRecordDTO::getData));
                String categoryName = categoryDTOMap.get(record.getCategoryId()).getTitle();
                productGroupbuyRecordDetail.setUserId(userId)
                        .setTxnId(productGroupbuyRecord.getTxnId())
                        .setProductGroupbuyId(productGroupbuyRecord.getProductGroupbuyId())
                        .setProductId(productGroupbuyRecord.getProductId())
                        .setProductGroupbuyRecordId(productGroupbuyRecord.getId())
                        .setUserShippingAddressId(record.getAddressId())
                        .setStandardInfo(JsonUtils.toJSON(standardInfo))
                        .setStandardTitle(categoryName + "/" + StringUtils.join(standardList,"/"))
                        .setStandardProductTitle(standardInfo.get("品牌"));
                productGroupbuyRecordDetailMapper.insert(productGroupbuyRecordDetail);
            });
        }
    }

    public Page<ProductGroupbuyRecordListDTO> queryProductGroupbuyRecordForList(Integer current,Integer size,Long userId,Integer process){
        List<ProductGroupbuyDTO> productGroupbuys = productGroupbuyService.queryGroupbuyPublishedList();
        Page page = new Page(current,size);
        QueryWrapper<ProductGroupbuyRecord> wrapper = new QueryWrapper();
        wrapper.eq("userId",userId);
        wrapper.ne("status",-1L);
        if (1 == process && CollectionUtils.isNotEmpty(productGroupbuys)){
            wrapper.notIn("productGroupbuyId",productGroupbuys.stream().map(ProductGroupbuyDTO::getId).collect(Collectors.toList()));
        }
        wrapper.orderByDesc("id");
        productGroupbuyRecordMapper.selectPage(page,wrapper);
        List<ProductGroupbuyRecord> groupbuyRecords = page.getRecords();
        if(CollectionUtils.isEmpty(groupbuyRecords)){
            return page;
        }
        page.setRecords(queryGroupbuyRecordOther(groupbuyRecords));
        return page;
    }

    public List<ProductGroupbuyRecordListDTO> queryGroupbuyRecordOther(List<ProductGroupbuyRecord> groupbuyRecords){
        Set<Long> groupbuyIds = groupbuyRecords.stream()
                .map(ProductGroupbuyRecord::getProductGroupbuyId)
                .collect(Collectors.toSet());
        Map<Long, ProductGroupbuyDTO> groupbuyMap = productGroupbuyService.queryProductGroupbuyByIds(groupbuyIds);

        Set<Long> productIds = groupbuyRecords.stream()
                .map(ProductGroupbuyRecord::getProductId)
                .collect(Collectors.toSet());
        Map<Long, ProductInfoDTO> productInfoMap = productService.queryProductGroupbuyByIds(productIds);

        List<ProductGroupbuyRecordListDTO> groupbuyRecordList = Lists.newArrayList();
        groupbuyRecords.stream().forEach(record ->{
            ProductGroupbuyDTO groupbuy = groupbuyMap.get(record.getProductGroupbuyId());
            if (groupbuy != null){
                ProductInfoDTO productInfoDTO = productInfoMap.get(groupbuy.getProductId());
                groupbuyRecordList.add(queryGroupbuyRecordOther(groupbuy,record,productInfoDTO));
            }
        });
        return groupbuyRecordList;
    }

    private ProductGroupbuyRecordListDTO queryGroupbuyRecordOther(ProductGroupbuyDTO groupbuy,ProductGroupbuyRecord record,ProductInfoDTO productInfoDTO){
        ProductGroupbuyRecordListDTO groupbuyRecordDTO = ProductGroupbuyRecordListDTO.builder().build()
                .setGroupbuyImg(productInfoDTO.getImgs())
                .setCategoryName(productInfoDTO.getCategoryName())
                .setTitle(groupbuy.getTitle())
                .setIsPublished(groupbuy.getIsPublished().intValue())
                .setQuantity(groupbuy.getBuyTotalQuantity())
                .setType(groupbuy.getType().intValue())
                .setMinDiscountQuantity(groupbuy.getMinDiscountQuantity())
                .setGroupbuyId(record.getProductGroupbuyId())
                .setGroupbuyRecordId(record.getId())
                .setGroupbuyRecordNo(record.getTxnId())
                .setStatus(record.getStatus().intValue())
                .setStatusName(ProductGroupbuyRecordEnum.getMessageByCode(record.getStatus().intValue()))
                .setCreateTime(DateTimeFormatter.ofPattern("yyyy.MM.dd").format(record.getCreateTime()))
                .setActivityEndDate(DateTimeFormatter.ofPattern("yyyy.MM.dd").format(groupbuy.getActivityEndDate()))
                .setGroupbuyRecordDetailList(queryProductGroupbuyRecordDetailByUser(record.getId(),record.getProductGroupbuyId(),record.getUserId()));
        return groupbuyRecordDTO;
    }

    public Page<ProductGroupbuyRecordListDTO> queryGroupbuyRecordPublished(Integer current,Integer size,Long userId){
        List<ProductGroupbuyDTO> productGroupbuys = productGroupbuyService.queryGroupbuyPublishedList();
        Page page = new Page(current,size);
        if (CollectionUtils.isEmpty(productGroupbuys)){
            return page;
        }
        QueryWrapper<ProductGroupbuyRecord> wrapper = new QueryWrapper();
        wrapper.in("productGroupbuyId",productGroupbuys.stream().map(ProductGroupbuyDTO::getId).collect(Collectors.toList()));
        wrapper.eq("userId",userId);
        wrapper.eq("status",SUCCESS.intValue());
        wrapper.orderByDesc("id");
        productGroupbuyRecordMapper.selectPage(page,wrapper);
        List<ProductGroupbuyRecord> groupbuyRecords = page.getRecords();
        if(CollectionUtils.isEmpty(groupbuyRecords)){
            return page;
        }
        page.setRecords(queryGroupbuyRecordOther(groupbuyRecords));
        return page;
    }

    public List<ProductGroupbuyRecordDetailListDTO> queryProductGroupbuyRecordDetailByUser(Long groupbuyRecordId,Long groupbuyId,Long userId){
        QueryWrapper<ProductGroupbuyRecordDetail> wrapper = new QueryWrapper();
        wrapper.eq("userId",userId);
        wrapper.eq("productGroupbuyRecordId",groupbuyRecordId);
        wrapper.eq("status",SUCCESS.intValue());
        wrapper.orderByAsc("isReachTarget");

        ProductGroupbuyStandard productGroupbuyStandard = productGroupbuyStandardService.queryLastDiscountByProductGroupbuyId(groupbuyId,ProductGroupbuyPublishEnum.YES.getCode());
        BigDecimal priceScale = new BigDecimal(0);
        Integer targetQuantity = 0;
        String reachTarrgetDate = "";
        if (productGroupbuyStandard != null){
            priceScale = productGroupbuyStandard.getPriceScale();
            targetQuantity = productGroupbuyStandard.getTargetQuantity();
            reachTarrgetDate = DateTimeFormatter.ofPattern("yyyy.MM.dd").format(productGroupbuyStandard.getReachTargetDate());
        }

        ProductGroupbuyStandard groupbuyMinStandard = productGroupbuyStandardService.queryLastDiscountByProductGroupbuyId(groupbuyId,ProductGroupbuyPublishEnum.NO.getCode());
        boolean flag = false;
        List<ProductGroupbuyRecordDetailListDTO> groupbuyRecordDetailList = Lists.newArrayList();
        List<ProductGroupbuyRecordDetail> list = productGroupbuyRecordDetailMapper.selectList(wrapper);
        for ( ProductGroupbuyRecordDetail detail: list) {
            ProductGroupbuyRecordDetailListDTO groupbuyRecordDetail = ProductGroupbuyRecordDetailListDTO.builder().build();
            if (3 == detail.getIsReachTarget().intValue()){
                groupbuyRecordDetail.setPriceScale(groupbuyMinStandard.getPriceScale());
                groupbuyRecordDetail.setTargetQuantity(groupbuyMinStandard.getTargetQuantity());
                groupbuyRecordDetail.setReachTargetDate("未公示");
            }else{
                if (ProductGroupbuyPublishEnum.YES.getCode().equals(detail.getIsReachTarget().intValue())){
                    groupbuyRecordDetail.setPriceScale(priceScale);
                    groupbuyRecordDetail.setTargetQuantity(targetQuantity);
                    groupbuyRecordDetail.setReachTargetDate(reachTarrgetDate);
                    flag = true;
                }else {
                    if (flag) {
                        groupbuyRecordDetail.setPriceScale(priceScale);
                        groupbuyRecordDetail.setTargetQuantity(targetQuantity);
                        groupbuyRecordDetail.setReachTargetDate("未达标");
                    } else {
                        groupbuyRecordDetail.setPriceScale(groupbuyMinStandard.getPriceScale());
                        groupbuyRecordDetail.setTargetQuantity(groupbuyMinStandard.getTargetQuantity());
                        groupbuyRecordDetail.setReachTargetDate("未达标");
                    }
                }
            }
            groupbuyRecordDetail.setStandardProductTitle(detail.getStandardProductTitle());
            groupbuyRecordDetailList.add(groupbuyRecordDetail);
        }
        return groupbuyRecordDetailList;
    }
}
