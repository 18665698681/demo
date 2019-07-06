package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.view.ProductLogisticsListDTO;
import com.dtlonline.shop.mapper.ProductLogisticsMapper;
import com.dtlonline.shop.model.ProductLogistics;
import io.alpha.app.core.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductLogisticsService {

    @Autowired
    private ProductLogisticsMapper productLogisticsMapper;

    public Map<String,ProductLogisticsListDTO> queryProductLogisticsForTxnId(Set<String> txnIds){
        QueryWrapper<ProductLogistics> wrapper = new QueryWrapper();
        wrapper.in("txnId",txnIds);
        List<ProductLogistics> logList = productLogisticsMapper.selectList(wrapper);
        return logList.stream().collect(Collectors.toMap(ProductLogistics::getTxnId, ProductLogistics::of));
    }
}
