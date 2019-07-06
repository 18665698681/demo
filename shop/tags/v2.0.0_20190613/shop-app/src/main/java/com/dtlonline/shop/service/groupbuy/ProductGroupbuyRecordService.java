package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.order.remote.OrderRemoteService;
import com.dtlonline.api.order.view.goods.OrderJournalListDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordDetailRequestDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordRequestDTO;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyPublishEnum;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyRecordEnum;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDeliveryDetailDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordDetailListDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordListDTO;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordDetailMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordMapper;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecord;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecordDetail;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import com.dtlonline.shop.service.CategoryService;
import com.dtlonline.shop.service.ProductService;
import com.dtlonline.shop.service.ShopService;
import com.dtlonline.shop.view.CategoryDTO;
import com.dtlonline.shop.view.ViewCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.util.SequenceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private ShopService shopService;

    @Autowired
    private OrderRemoteService orderRemoteService;

    @Transactional(rollbackFor = Exception.class)
    public void issueProductGroupbuyRecord(ProductGroupbuyRecordRequestDTO groupbuyRecord,Long userId){
        ProductGroupbuy groupbuy = productGroupbuyService.queryGroupbuy(groupbuyRecord.getProductGroupbuyId());
        if (LocalDateTime.now().isBefore(groupbuy.getBuyBeginDate()) || LocalDateTime.now().isAfter(groupbuy.getBuyEndDate()) ){
            throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"未在团购报名时间之内,无法报名该团购");
        }
        ProductInfoDTO productInfoDTO = productService.queryProductInfo(groupbuy.getProductId());
        if (null == productInfoDTO){
            throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购不存在,请联系客服");
        }
        ShopDTO shopDTO = shopService.queryById(productInfoDTO.getShopId());
        if (null != shopDTO && shopDTO.getUserId().equals(userId)){
            throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"不能参与自己发布的团购商品");
        }
        Integer totalQuantity = groupbuyRecord.getGroupbuyRecordDetailList()
                .stream()
                .map(d -> d.getQuantity())
                .reduce(0, Integer::sum);
        if (totalQuantity < productInfoDTO.getMinDeal()){
            throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),
                    "团购数量不足"+productInfoDTO.getMinDeal()+"吨，请继续添加");
        }
        ProductGroupbuyRecord productGroupbuyRecord = ProductGroupbuyRecord.builder().build();
        BeanUtils.copyProperties(groupbuyRecord,productGroupbuyRecord);
        productGroupbuyRecord.setUserId(userId)
                .setQuantity(totalQuantity)
                .setProductGroupbuyId(groupbuy.getId())
                .setProductId(groupbuy.getProductId())
                .setBalanceType(groupbuy.getBalanceType());
        productGroupbuyRecordMapper.insert(productGroupbuyRecord);
        issueProductGroupbuyRecordDetail(groupbuyRecord.getGroupbuyRecordDetailList(),groupbuy,productGroupbuyRecord,userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void issueProductGroupbuyRecordDetail(List<ProductGroupbuyRecordDetailRequestDTO> groupbuyRecordDetailList,ProductGroupbuy groupbuy,ProductGroupbuyRecord productGroupbuyRecord,Long userId){
        if (CollectionUtils.isNotEmpty(groupbuyRecordDetailList)){
            Set<Long> categoryIds = groupbuyRecordDetailList
                    .stream()
                    .map(ProductGroupbuyRecordDetailRequestDTO::getCategoryId)
                    .collect(Collectors.toSet());
            Map<Long, CategoryDTO> categoryDTOMap = categoryService.queryCategoryMap(categoryIds);
            Integer i = 0;
            for (ProductGroupbuyRecordDetailRequestDTO record: groupbuyRecordDetailList) {
                List<ProductGroupbuyDeliveryDetailDTO> deliverList =
                        JsonUtils.parseJSON(record.getDeliveryData(),new TypeReference<List<ProductGroupbuyDeliveryDetailDTO>>(){});

                //规则1：首批到货日期和剩余到货时间的月份必须为团购批次的下一个月，且不可更改。
                LocalDate yearMonth = groupbuy.getYearMonth().plusMonths(1L);
                ProductGroupbuyDeliveryDetailDTO delivery = deliverList.get(0);
                ProductGroupbuyDeliveryDetailDTO lastDelivery = deliverList.get(1);
                LocalDate deliveryDate = delivery.getDeliveryDate();
                LocalDate lastDeliveryDate = lastDelivery.getDeliveryDate();
                i++;
                if (yearMonth.getYear() != deliveryDate.getYear()
                        || yearMonth.getMonthValue() != deliveryDate.getMonthValue()){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i  +"的首批到货日期填写错误");
                }
                if (yearMonth.getYear() != lastDeliveryDate.getYear()
                        || yearMonth.getMonthValue() != lastDeliveryDate.getMonthValue()){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i  +"的剩余到货日期填写错误");
                }
                //规则2：首批到货日期和剩余到货时间的日期是当前日期+30天（比如当前日期为6月13日，则日期选项为 7月12号至 7月31号）

                //规则3：剩余到货日期必须大于首批到货日期
                if (deliveryDate.isAfter(lastDeliveryDate)){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i  +"的剩余到货日期必须大于首批到货日期");
                }
                //规则4：首批到货量必须大于等于单批最小配送量
                if (delivery.getDeliveryQuantity() < record.getMinDeliveryQuantity()){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i +"的首批到货量必须大于等于单批最小配送量");
                }
                //规则5：剩余到货量必须大于等于单批最小配送量
                if (lastDelivery.getDeliveryQuantity() < record.getMinDeliveryQuantity()){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i +"的剩余到货量必须大于等于单批最小配送量");
                }
                //规则6：首批到货量 + 剩余到货量 = 团购数量
                Integer totalQuantity = delivery.getDeliveryQuantity() + lastDelivery.getDeliveryQuantity();
                if (!record.getQuantity().equals(totalQuantity)){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i + "的发货总数与团购数量不相符");
                }
                //规则7：单批最少配送量不得大于团购数量
                if (record.getMinDeliveryQuantity() > record.getQuantity()){
                    throw new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(),"团购信息"+ i + "的单批最少配送量不得大于团购数量");
                }
                ProductGroupbuyRecordDetail productGroupbuyRecordDetail = ProductGroupbuyRecordDetail.builder().build();
                BeanUtils.copyProperties(record,productGroupbuyRecordDetail);
                List<ProductStandardRecordDTO> sortedStandardList = record.getStandardList().stream()
                        .sorted((u1, u2) -> u1.getStandardId().compareTo(u2.getStandardId()))
                        .collect(Collectors.toList());

                List<String> standardList = sortedStandardList.stream()
                        .map(ProductStandardRecordDTO::getData)
                        .collect(Collectors.toList());

                Map<String, String> standardInfo = sortedStandardList.stream()
                        .collect(Collectors.toMap(ProductStandardRecordDTO::getStandardName, ProductStandardRecordDTO::getData));

                String categoryName = categoryDTOMap.get(record.getCategoryId()).getTitle();
                productGroupbuyRecordDetail.setUserId(userId)
                        .setTxnId(SequenceUtils.getSequence())
                        .setProductGroupbuyId(productGroupbuyRecord.getProductGroupbuyId())
                        .setProductId(productGroupbuyRecord.getProductId())
                        .setProductGroupbuyRecordId(productGroupbuyRecord.getId())
                        .setUserShippingAddressId(record.getAddressId())
                        .setStandardInfo(JsonUtils.toJSON(standardInfo))
                        .setStandardTitle(categoryName + "/" + StringUtils.join(standardList,"/"))
                        .setStandardProductTitle(standardInfo.get("品牌"));
                productGroupbuyRecordDetailMapper.insert(productGroupbuyRecordDetail);
            }
        }
    }

    public Page<ProductGroupbuyRecordListDTO> queryProductGroupbuyRecordForList(Integer current,Integer size,Long userId,Integer process){
        List<ProductGroupbuyDTO> productGroupbuys = productGroupbuyService.queryGroupbuyPublishedList();
        Page page = new Page(current,size);
        QueryWrapper<ProductGroupbuyRecord> wrapper = new QueryWrapper();
        wrapper.eq("userId",userId);
        wrapper.ne("status",-1L);
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
                .setQuantity(record.getQuantity())
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
        wrapper.in("productGroupbuyId",productGroupbuys.stream()
                .map(ProductGroupbuyDTO::getId)
                .collect(Collectors.toList()));
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
        Set<String> txnIds = list.stream().map(ProductGroupbuyRecordDetail::getTxnId).collect(Collectors.toSet());
        Map<String, OrderJournalListDTO> orderJournalMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(txnIds)){
            orderJournalMap = orderRemoteService.queryOrdersByTxnIds(txnIds);
        }
        for ( ProductGroupbuyRecordDetail detail: list) {
            ProductGroupbuyRecordDetailListDTO groupbuyRecordDetail = ProductGroupbuyRecordDetailListDTO.builder().build();
            if (3 == detail.getIsReachTarget().intValue()){
                groupbuyRecordDetail.setPriceScale("--");
                groupbuyRecordDetail.setTargetQuantity("--");
                groupbuyRecordDetail.setReachTargetDate("未公示");
            }else{
                if (ProductGroupbuyPublishEnum.YES.getCode().equals(detail.getIsReachTarget().intValue())){
                    groupbuyRecordDetail.setPriceScale(priceScale + "%");
                    groupbuyRecordDetail.setTargetQuantity(targetQuantity + "吨");
                    groupbuyRecordDetail.setReachTargetDate(reachTarrgetDate);
                    flag = true;
                }else {
                    if (flag) {
                        groupbuyRecordDetail.setPriceScale(priceScale + "%");
                        groupbuyRecordDetail.setTargetQuantity(targetQuantity + "吨");
                        groupbuyRecordDetail.setReachTargetDate("未达标");
                    } else {
                        groupbuyRecordDetail.setPriceScale(groupbuyMinStandard.getPriceScale() + "%");
                        groupbuyRecordDetail.setTargetQuantity(groupbuyMinStandard.getTargetQuantity() + "吨");
                        groupbuyRecordDetail.setReachTargetDate("未达标");
                    }
                }
                OrderJournalListDTO orderJournalListDTO = orderJournalMap.get(detail.getTxnId());
                if ( null != orderJournalListDTO){
                    groupbuyRecordDetail.setOrderNo(orderJournalListDTO.getOrderNo());
                }
            }

            groupbuyRecordDetail.setStandardProductTitle(detail.getStandardProductTitle());
            groupbuyRecordDetailList.add(groupbuyRecordDetail);
        }
        return groupbuyRecordDetailList;
    }
}
