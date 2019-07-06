package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.view.ProductStandardRecordListDTO;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductStandardRecordService extends BaseService {

    @Autowired
    public ProductStandardRecordDao productStandardRecordDao;

    public List<ProductStandardRecordListDTO> queryProductStandardList(ProductStandardRecordDTO productStandardRecordDTO) {
        List<ProductStandardRecord> productRecords = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardRecordListDTO> standardRecordList = new ArrayList<>(productRecords.size());
        productRecords.forEach(addr -> standardRecordList.add(ProductStandardRecordListDTO.of(addr)));
        return standardRecordList;
    }

    public Map<String,List<ProductStandardRecordListDTO>> queryProductStandardListMap(Set<String> txnIdSet) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status",SUCCESS);
        wrapper.in("txnId",txnIdSet);
        List<ProductStandardRecord> productRecords = productStandardRecordDao.selectList(wrapper);
        Map<String,List<ProductStandardRecordListDTO>> listMap = productRecords.stream()
                .collect(Collectors.groupingBy(ProductStandardRecord::getTxnId,Collectors
                        .mapping(ProductStandardRecordListDTO::of,Collectors.toList())));
        return listMap;
    }
}
