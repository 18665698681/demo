package com.dtlonline.shop.service;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.view.ProductStandardRecordListDTO;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
