package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.view.ProductPositionDTO;
import com.dtlonline.shop.mapper.ProductPositionDao;
import com.dtlonline.shop.model.ProductPosition;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductPositionService extends BaseService {

    @Autowired
    private ProductPositionDao productPositionDao;

    public Map<String,ProductPositionDTO> queryProductPosition(Set<String> txnIds){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in(txnIds);
        List<ProductPosition> list = productPositionDao.selectList(wrapper);
        return list.stream().collect(Collectors.toMap(ProductPosition::getTxnId, ProductPosition::of));
    }

    public ProductPositionDTO queryProductPositionById(String txnId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("txnId",txnId);
        ProductPosition productPosition = productPositionDao.selectOne(wrapper);
        ProductPositionDTO positionDTO = new ProductPositionDTO();
        BeanUtils.copyProperties(productPosition,positionDTO);
        return positionDTO;
    }
}
