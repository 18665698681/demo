package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.view.ProductCarInfoDTO;
import com.dtlonline.shop.mapper.ProductCarInfoDao;
import com.dtlonline.shop.model.ProductCarInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductCarInfoService {

    @Autowired
    private ProductCarInfoDao productCarInfoDao;

    public Map<String,ProductCarInfoDTO> queryProductCarInfoList(Set<String> txnIds){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("txnId",txnIds);
        List<ProductCarInfo> list = productCarInfoDao.selectList(wrapper);
        return list.stream().collect(Collectors.toMap(ProductCarInfo::getTxnId, ProductCarInfo::of));
    }
}
