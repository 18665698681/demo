package com.dtlonline.shop.controller.internal.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsExtractDTOI;
import com.dtlonline.api.shop.remote.store.StoreGoodsExtractRemoteService;
import com.dtlonline.shop.service.store.StoreGoodsExtractService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class StoreGoodsExtractController extends BaseController implements StoreGoodsExtractRemoteService {

    @Autowired
    private StoreGoodsExtractService storeGoodsExtractService;

    @Override
    @PostMapping("/stores/extract")
    public RestResult<Void> issueStoreGoodsExtract(StoreGoodsExtractDTOI storeGoodsExtract, Long userId){
        storeGoodsExtractService.extractApply(storeGoodsExtract,userId);
        return RestResult.success();
    }

    @Override
    @GetMapping("/stores/extract")
    public Page queryStoreGoodsExtractForList(Integer current,Integer size,Long userId){
        return storeGoodsExtractService.queryList(current,size,userId);
    }
}
