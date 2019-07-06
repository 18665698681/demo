package com.dtlonline.api.shop.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.ProductSupplyDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "${apps.shop.name:shop-app}",contextId = "ProductRemoteService")
public interface ProductRemoteService {

    @GetMapping("/internal/products/{productId}")
    ProductInfoDTO queryProductInfo(@PathVariable("productId") Long productId);

    @GetMapping("/internal/products/list")
    Map<Long, ProductInfoDTO> queryProductInfo(@RequestParam("id") Set<Long> ids);

    @GetMapping("/internal/products")
    Page queryProductSupplyForList(@RequestParam(value = "current",defaultValue = "1") Integer current,@RequestParam(value = "size",defaultValue = "10") Integer size,@RequestParam("userId") Long userId,@RequestParam("shopId") Long shopId);

    @GetMapping("/internal/products/detail/{productId}")
    ProductSupplyDetailDTO queryProductDetailSupply(@PathVariable("productId") Long productId);
}
