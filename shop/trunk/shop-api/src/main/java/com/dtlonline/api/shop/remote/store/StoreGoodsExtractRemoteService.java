package com.dtlonline.api.shop.remote.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsExtractDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsExtractDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "StoreGoodsExtractRemoteService")
public interface StoreGoodsExtractRemoteService {

    @PostMapping("/internal/stores/extract")
    RestResult<Void> issueStoreGoodsExtract(@RequestBody StoreGoodsExtractDTOI storeGoodsExtract, @RequestParam("userId") Long userId);

    @GetMapping("/internal/stores/extract")
    Page<StoreGoodsExtractDTO> queryStoreGoodsExtractForList(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("userId") Long userId);
}
