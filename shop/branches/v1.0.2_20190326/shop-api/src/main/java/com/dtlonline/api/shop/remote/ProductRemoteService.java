package com.dtlonline.api.shop.remote;

import com.dtlonline.api.shop.view.ProductInfoDTO;
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

}
