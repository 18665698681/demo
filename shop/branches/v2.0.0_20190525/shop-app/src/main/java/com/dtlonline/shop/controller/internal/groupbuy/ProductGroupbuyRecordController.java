package com.dtlonline.shop.controller.internal.groupbuy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordRequestDTO;
import com.dtlonline.api.shop.remote.groupbuy.ProductGroupbuyRecordRemoteService;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordListDTO;
import com.dtlonline.shop.service.groupbuy.ProductGroupbuyRecordService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/products/groupbuy")
public class ProductGroupbuyRecordController extends BaseController implements ProductGroupbuyRecordRemoteService {

    @Autowired
    private ProductGroupbuyRecordService productGroupbuyRecordService;

    @Override
    @PostMapping("/records")
    public RestResult<Void> issueProductGroupbuyRecord(@RequestBody ProductGroupbuyRecordRequestDTO groupbuyRecord, Long userId){
        productGroupbuyRecordService.issueProductGroupbuyRecord(groupbuyRecord,userId);
        return RestResult.success();
    }

    @Override
    @GetMapping("/records/registration")
    public RestResult<Page<ProductGroupbuyRecordListDTO>> queryProductGroupbuyRecordForList(Integer current, Integer size, Long userId,Integer process){
        return RestResult.success(productGroupbuyRecordService.queryProductGroupbuyRecordForList(current,size,userId,process));
    }

    @Override
    @GetMapping("/records/published")
    public RestResult<Page<ProductGroupbuyRecordListDTO>> queryGroupbuyRecordPublished(Integer current, Integer size, Long userId){
        return RestResult.success(productGroupbuyRecordService.queryGroupbuyRecordPublished(current,size,userId));
    }
}
