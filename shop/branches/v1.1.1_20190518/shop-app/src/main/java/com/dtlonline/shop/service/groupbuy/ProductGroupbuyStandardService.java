package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyStandardDao;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGroupbuyStandardService extends BaseService {

    @Autowired
    private ProductGroupbuyStandardDao productGroupbuyStandardDao;

    /**
     * 根据团购ID获取对应的团购规则列表
     * @param productGroupbuyId 团购活动ID
     * @return List<ProductGroupbuyStandard> 团购规则列表
     */
    public List<ProductGroupbuyStandard> queryListByProductGroupbuyId(Long productGroupbuyId) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", 1);
        wrapper.eq("productGroupbuyId", productGroupbuyId);
        wrapper.orderByAsc("targetWeight");
        return productGroupbuyStandardDao.selectList(wrapper);
    }

    /**
     * 新增
     * @param productGroupbuyStandard 团购活动规则实体
     * @return void
     */
    public void save(ProductGroupbuyStandard productGroupbuyStandard) {

        productGroupbuyStandardDao.insert(productGroupbuyStandard);
    }

}
