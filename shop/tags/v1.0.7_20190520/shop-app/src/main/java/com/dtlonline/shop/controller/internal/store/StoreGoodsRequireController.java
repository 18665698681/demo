package com.dtlonline.shop.controller.internal.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsRequireDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsRequireQueryDTOI;
import com.dtlonline.api.shop.remote.store.StoreGoodsRequireRemoteService;
import com.dtlonline.api.shop.view.store.StoreGoodsRequireDTO;
import com.dtlonline.shop.service.store.StoreGoodsRequireService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class StoreGoodsRequireController extends BaseController implements StoreGoodsRequireRemoteService {

    @Autowired
    private StoreGoodsRequireService storeGoodsRequireService;

    @Override
    @PostMapping(value = "/stores/goods/requires")
    public RestResult<Void> issueStoreGoodsRequire(@RequestBody StoreGoodsRequireDTOI storeGoodsRequire, Long userId) {
        storeGoodsRequireService.publishMulti(storeGoodsRequire, userId);
        return RestResult.success();
    }

    @Override
    @PostMapping("/stores/goods/list")
    public RestResult<Page<StoreGoodsRequireDTO>> queryList(@RequestBody StoreGoodsRequireQueryDTOI storeGoodsRequireDTOI) {
        return RestResult.success(storeGoodsRequireService.queryList(storeGoodsRequireDTOI));
    }

    @Override
    @GetMapping("/stores/goods/requires")
    public RestResult<Page<StoreGoodsRequireDTO>> queryMyStoreGoodsRequireList(@RequestParam("current") Integer current,
                                                                               @RequestParam("size") Integer size,
                                                                               @RequestParam("type") Integer type,
                                                                               @RequestParam("userId") Long userId,
                                                                               @RequestParam("hideOffSale")Boolean hideOffSale){
        StoreGoodsRequireQueryDTOI storeGoodsRequireDTOI = new StoreGoodsRequireQueryDTOI();
        storeGoodsRequireDTOI.setCurrent(current);
        storeGoodsRequireDTOI.setSize(size);
        storeGoodsRequireDTOI.setType(type);
        storeGoodsRequireDTOI.setUserId(userId);
        storeGoodsRequireDTOI.setHideOffSale(hideOffSale);
        return RestResult.success(storeGoodsRequireService.queryList(storeGoodsRequireDTOI));
    }

    @Override
    @GetMapping("/stores/goods/detail/{id}")
    public RestResult<StoreGoodsRequireDTO> queryOne(@PathVariable("id") Long id) {
        return RestResult.success(storeGoodsRequireService.queryOne(id));
    }
}
