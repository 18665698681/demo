package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyPublishEnum;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyStandardMapper;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import io.alpha.app.core.base.BaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGroupbuyStandardService extends BaseService {

    @Autowired
    private ProductGroupbuyStandardMapper productGroupbuyStandardMapper;

    /**
     * 获取某个团购活动的最终优惠比例
     *
     * @param productGroupbuyId 团购活动ID
     * @return void
     */
    public ProductGroupbuyStandard queryLastDiscountByProductGroupbuyId(Long productGroupbuyId,Integer isReachTarget) {
        QueryWrapper<ProductGroupbuyStandard> wrapper = new QueryWrapper();
        wrapper.eq("status", SUCCESS.intValue());
        wrapper.eq("productGroupbuyId", productGroupbuyId);
        if (ProductGroupbuyPublishEnum.YES.getCode().equals(isReachTarget)){
            wrapper.eq("isReachTarget", 1);
            wrapper.isNotNull("reachTargetDate");
            wrapper.orderByDesc("targetQuantity");
        }else {
            wrapper.orderByAsc("targetQuantity");
        }
        List<ProductGroupbuyStandard> list = productGroupbuyStandardMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
