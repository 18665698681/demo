package com.dtlonline.shop.controller.internal;

import com.dtlonline.api.shop.command.ProductRecordParamDTO;
import com.dtlonline.api.shop.remote.ProductRecordRemoteService;
import com.dtlonline.shop.service.ProductRecordService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
