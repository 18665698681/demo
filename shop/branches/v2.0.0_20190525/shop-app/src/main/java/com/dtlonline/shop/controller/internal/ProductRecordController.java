package com.dtlonline.shop.controller.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductRecordParamDTO;
import com.dtlonline.api.shop.remote.ProductRecordRemoteService;
import com.dtlonline.shop.service.ProductRecordService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/internal")
public class ProductRecordController extends BaseController implements ProductRecordRemoteService {

    @Autowired
    private ProductRecordService productRecordService;

    @Override
    @PostMapping("/products")
    public void issueProductRecord(@RequestBody @Valid ProductRecordParamDTO productRecordParam, Long userId){
        productRecordService.issueProductRecord(productRecordParam,userId);
    }

    @Override
    @GetMapping("/products/list/{type}")
    public Page queryProductRecordSupplyForList(Integer current,Integer size,Long userId,Integer type){
        return productRecordService.queryProductRecordSupplyForList(current,size,userId,type);
    }
}
