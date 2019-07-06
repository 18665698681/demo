package com.dtlonline.api.shop.remote.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsAuthRecordDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsAuthRecordDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsMergeQuantityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "StoreGoodsAuthRecordRemoteService")
public interface StoreGoodsAuthRecordRemoteService {

    @PostMapping("/internal/stores")
    void issueStoreGoodsAuthRecord(@RequestBody StoreGoodsAuthRecordDTOI storeGoodsAuthRecord, @RequestParam("userId") Long userId);

    @GetMapping("/internal/stores/record")
    Page<StoreGoodsAuthRecordDTO> queryStoreGoodsAuthRecordList(@RequestParam("current") Integer current,@RequestParam("size")Integer size ,@RequestParam("userId") Long userId);

    @GetMapping("/internal/stores/pass")
    Page<StoreGoodsDTO> queryStoreGoodsAuthPassList(@RequestParam("current") Integer current,@RequestParam("size")Integer size ,@RequestParam("userId") Long userId,@RequestParam(name = "storeId",required = false)Long storeId,@RequestParam(name = "productTitle",required = false)String productTitle);

    @GetMapping("/internal/stores")
    Page<StoreGoodsMergeQuantityDTO> queryMyStoreGoodsList(@RequestParam("current") Integer current,@RequestParam("size")Integer size ,@RequestParam("userId") Long userId);

    @GetMapping("/internal/stores/record/{goodsId}")
    StoreGoodsAuthRecordDTO queryStoreGoodsAuthorDetailForId(@PathVariable("goodsId") Long goodsId);

    @GetMapping("/internal/stores/{goodsId}")
    StoreGoodsDTO queryStoreGoodsDetailForId(@PathVariable("goodsId") Long goodsId);
}
