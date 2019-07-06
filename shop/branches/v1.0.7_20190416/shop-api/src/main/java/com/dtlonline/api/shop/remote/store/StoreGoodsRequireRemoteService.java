package com.dtlonline.api.shop.remote.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsRequireDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsRequireQueryDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsRequireDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "${apps.shop.name:shop-app}", contextId = "StoreGoodsRequireRemoteService")
public interface StoreGoodsRequireRemoteService {

    @PostMapping(value = "/internal/stores/goods/requires")
    RestResult<Void> issueStoreGoodsRequire(@RequestBody StoreGoodsRequireDTOI storeGoodsRequire, @RequestParam("userId") Long userId);

    @PostMapping("/internal/stores/goods/list")
    RestResult<Page<StoreGoodsRequireDTO>> queryList(@RequestBody StoreGoodsRequireQueryDTOI storeGoodsRequireDTOI);

    @GetMapping("/internal/stores/goods/requires")
    RestResult<Page<StoreGoodsRequireDTO>> queryMyStoreGoodsRequireList(@RequestParam("current") Integer current,
                                                                        @RequestParam("size") Integer size,
                                                                        @RequestParam("type") Integer type,
                                                                        @RequestParam("userId") Long userId,
                                                                        @RequestParam("hideOffSale") Boolean hideOffSale);

    @GetMapping("/internal/stores/goods/detail/{id}")
    RestResult<StoreGoodsRequireDTO> queryOne(@PathVariable("id") Long id);
}
