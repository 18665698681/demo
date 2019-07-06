package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyPublishEnum;
import com.dtlonline.api.shop.view.CategoryDTOI;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyBrandDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyStandardDTO;
import com.dtlonline.shop.constant.auction.AuctionProcessStatus;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordDetailMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyRecordMapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyStandardMapper;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecord;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecordDetail;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import com.dtlonline.shop.service.CategoryService;
import com.dtlonline.shop.service.ProductRecordService;
import com.dtlonline.shop.service.ProductService;
import com.dtlonline.shop.view.CategoryDTO;
import com.dtlonline.shop.view.ViewCode;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 团购活动service
 */
@Service
public class ProductGroupbuyService extends BaseService {

    @Autowired
    private ProductGroupbuyMapper productGroupbuyMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductGroupbuyStandardMapper productGroupbuyStandardMapper;

    @Autowired
    private ProductRecordService productRecordService;

    @Autowired
    private ProductGroupbuyRecordMapper productGroupbuyRecordMapper;

    @Autowired
    private ProductGroupbuyRecordDetailMapper productGroupbuyRecordDetailMapper;

    public Page<ProductGroupbuyDTO> queryListInPage(Integer current, Integer size, Integer process) {
        Page page = new Page(current, size);
        QueryWrapper<ProductGroupbuy> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        LocalDateTime date = LocalDateTime.now();
        if (AuctionProcessStatus.BEFORE.getCode().equals(process)) {
            wrapper.orderByAsc("buyBeginDate");
            wrapper.gt("buyBeginDate", date);
        }
        if (AuctionProcessStatus.GOINGON.getCode().equals(process)) {
            wrapper.orderByAsc("isTop");
            wrapper.orderByAsc("buyEndDate");
            wrapper.le("buyBeginDate", date);
            wrapper.ge("buyEndDate", date);
        }
        if (AuctionProcessStatus.OVER.getCode().equals(process)) {
            wrapper.orderByDesc("activityEndDate");
            wrapper.lt("activityEndDate", date);
        }
        productGroupbuyMapper.selectPage(page, wrapper);
        List<ProductGroupbuy> groupbuys = page.getRecords();
        return page.setRecords(getGroupbuyDTOList(groupbuys));
    }

    private List<ProductGroupbuyDTO> getGroupbuyDTOList(List<ProductGroupbuy> groupbuys) {
        if (groupbuys.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Set<Long> productIds = groupbuys.stream().map(ProductGroupbuy::getProductId).collect(Collectors.toSet());
        Map<Long, ProductInfoDTO> products = productService.queryProductInfoByIds(productIds);
        Collection<ProductInfoDTO> sss = products.values();
        Set<Long> categoryIds = sss.stream().map(ProductInfoDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryDTOs = categoryService.queryCategoryMap(categoryIds);
        List<ProductGroupbuyStandard> maxPriceScales = productGroupbuyStandardMapper.selectMaxScale(productIds);
        Map<Long, BigDecimal> maxPriceScalesMap = maxPriceScales.stream().filter(s -> s != null).collect(Collectors.toMap(ProductGroupbuyStandard::getProductId, ProductGroupbuyStandard::getPriceScale));
        return groupbuys.stream().map(d -> of(d, products.get(d.getProductId()), categoryDTOs, maxPriceScalesMap.get(d.getProductId()))).collect(Collectors.toList());
    }

    public ProductGroupbuyDTO queryGroupbuyDetails(Long recordId) {
        ProductGroupbuy groupbuy = queryGroupbuy(recordId);
        List<ProductGroupbuyStandard> standards = queryGroupbuyStandard(recordId);
        ProductInfoDTO product = productService.queryProductInfo(groupbuy.getProductId());
        product.setImgList(productRecordService.queryImgsOtherExtra(product.getImgs()));
        CategoryDTOI categoryDTOI = categoryService.queryCategorysById(product.getCategoryId());
        List<ProductGroupbuyStandardDTO> standardDTOS = standards.stream().map(d -> ofStandardDTO(d)).collect(Collectors.toList());
        ProductGroupbuyStandard maxPriceScales = productGroupbuyStandardMapper.selectMaxScaleByProductId(groupbuy.getProductId());
        List<ProductGroupbuyRecord> records = queryGroupbuyRecords(groupbuy);
        List<ProductGroupbuyBrandDTO> brandDTOS = queryBrandDTOs(records);
        ProductGroupbuyDTO dto = of(groupbuy, product, categoryDTOI, standardDTOS, maxPriceScales.getPriceScale());
        dto.setBrandDTOS(brandDTOS);
        dto.setJoinUserQuantity(records.isEmpty()?0:records.size());
        return dto;
    }

    public List<ProductGroupbuyBrandDTO> queryBrandDTOs(List<ProductGroupbuyRecord> records) {
        if (records == null || records.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Set<Long> productGroupbuyRecordIds = records.stream().map(ProductGroupbuyRecord::getId).collect(Collectors.toSet());
        QueryWrapper<ProductGroupbuyRecordDetail> detailWrapper = new QueryWrapper<>();
        detailWrapper.in("productGroupbuyRecordId", productGroupbuyRecordIds);
        List<ProductGroupbuyRecordDetail> details = productGroupbuyRecordDetailMapper.selectList(detailWrapper);
        Map<String, IntSummaryStatistics> collect = details.stream().collect(Collectors.groupingBy(ProductGroupbuyRecordDetail::getStandardProductTitle, Collectors.summarizingInt(ProductGroupbuyRecordDetail::getQuantity)));
        List<ProductGroupbuyBrandDTO> brands = collect.entrySet().stream().map(e -> new ProductGroupbuyBrandDTO(e.getKey(), (int) e.getValue().getSum())).collect(Collectors.toList());
        return brands;
    }

    private List<ProductGroupbuyRecord> queryGroupbuyRecords(ProductGroupbuy groupbuy) {
        QueryWrapper<ProductGroupbuyRecord> recordWrapper = new QueryWrapper<>();
        recordWrapper.eq("productId", groupbuy.getProductId());
        recordWrapper.eq("status", 1);
        List<ProductGroupbuyRecord> records = productGroupbuyRecordMapper.selectList(recordWrapper);
        if (records.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return records;
    }

    private List<ProductGroupbuyStandard> queryGroupbuyStandard(Long groupbuyId) {
        QueryWrapper<ProductGroupbuyStandard> wrapper = new QueryWrapper<>();
        wrapper.eq("productGroupbuyId", groupbuyId);
        return productGroupbuyStandardMapper.selectList(wrapper);
    }

    public ProductGroupbuy queryGroupbuy(Long recordId) {
        ProductGroupbuy groupbuy = productGroupbuyMapper.selectById(recordId);
        Optional.ofNullable(groupbuy).orElseThrow(() -> new ProductException(ViewCode.GROUPBUY_FAILURE.getCode(), "团购信息异常"));
        return groupbuy;
    }


    public Map<Long, ProductGroupbuyDTO> queryProductGroupbuyByIds(Set<Long> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", ids);
        wrapper.ne("status",-1L);
        List<ProductGroupbuy> list = productGroupbuyMapper.selectList(wrapper);
        Map<Long, ProductGroupbuyDTO> dtos = list.stream().collect(Collectors.toMap(ProductGroupbuy::getId, this::of));
        return dtos;
    }

    public List<ProductGroupbuyDTO> queryGroupbuyPublishedList() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("isPublished", ProductGroupbuyPublishEnum.YES.getCode());
        wrapper.ne("status",-1L);
        List<ProductGroupbuy> list = productGroupbuyMapper.selectList(wrapper);
        return list.stream().map(this::of).collect(Collectors.toList());
    }

    private ProductGroupbuyStandardDTO ofStandardDTO(ProductGroupbuyStandard standard) {
        ProductGroupbuyStandardDTO dto = new ProductGroupbuyStandardDTO();
        BeanUtils.copyProperties(standard, dto);
        return dto;
    }

    private ProductGroupbuyDTO of(ProductGroupbuy groupbuy) {
        ProductGroupbuyDTO dto = new ProductGroupbuyDTO();
        BeanUtils.copyProperties(groupbuy, dto);
        return dto;
    }

    private ProductGroupbuyDTO of(ProductGroupbuy groupbuy, ProductInfoDTO product, CategoryDTOI categorys, List<ProductGroupbuyStandardDTO> standardDTOS, BigDecimal maxPriceScale) {
        ProductGroupbuyDTO dto = new ProductGroupbuyDTO();
        BeanUtils.copyProperties(groupbuy, dto);
        dto.setCategoryDTOI(categorys);
        dto.setProductInfoDTO(product);
        dto.setProductGroupbuyStandardDTO(standardDTOS);
        dto.setMaxPriceScale(maxPriceScale);
        LocalDate date = LocalDate.now();
        if (date.isBefore(groupbuy.getBuyBeginDate())) {
            dto.setProcessStatus(AuctionProcessStatus.BEFORE.getCode());
        } else if (date.isAfter(groupbuy.getActivityEndDate())) {
            dto.setProcessStatus(AuctionProcessStatus.OVER.getCode());
        } else {
            dto.setProcessStatus(AuctionProcessStatus.GOINGON.getCode());
        }
        return dto;
    }

    private ProductGroupbuyDTO of(ProductGroupbuy groupbuy, ProductInfoDTO product, Map<Long, CategoryDTO> categorys, BigDecimal maxPriceScale) {
        ProductGroupbuyDTO dto = new ProductGroupbuyDTO();
        BeanUtils.copyProperties(groupbuy, dto);
        if (product != null && categorys != null && categorys.get(product.getCategoryId()) != null) {
            CategoryDTO categoryDTO = categorys.get(product.getCategoryId());
            CategoryDTOI categoryDTOI = new CategoryDTOI();
            BeanUtils.copyProperties(categoryDTO, categoryDTOI);
            dto.setCategoryDTOI(categoryDTOI);
        }
        dto.setProductInfoDTO(product);
        dto.setMaxPriceScale(maxPriceScale);
        return dto;
    }


}
