package com.dtlonline.api.shop.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductRecordParamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${apps.shop.name:shop-app}",contextId = "ProductRecordRemoteService")
public interface ProductRecordRemoteService {

    @PostMapping("/internal/products")
    void issueProductRecord(@RequestBody ProductRecordParamDTO productRecordParam, @RequestParam("userId") Long userId);

    @GetMapping("/internal/products/list/{type}")
    Page queryProductRecordSupplyForList(@RequestParam("current") Integer current,@RequestParam("size") Integer size,@RequestParam("userId") Long userId,@PathVariable("type") Integer type);
}
