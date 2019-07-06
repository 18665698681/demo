package com.dtlonline.api.shop.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.view.ProductHomeCountDTO;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.ProductSupplyDTO;
import com.dtlonline.api.shop.view.ProductSupplyDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "${apps.shop.name:shop-app}",contextId = "ProductRemoteService")
public interface ProductRemoteService {

    @GetMapping("/internal/products/{productId}")
    ProductInfoDTO queryProductInfo(@PathVariable("productId") Long productId);

    @GetMapping("/internal/products/list")
    Map<Long, ProductInfoDTO> queryProductInfo(@RequestParam("id") Set<Long> ids);

    @GetMapping("/internal/products")
    Page queryProductSupplyForList(@RequestParam(name = "current",defaultValue = "1") Integer current,@RequestParam(name = "size",defaultValue = "10") Integer size,@RequestParam("keywords") String keywords);

    @GetMapping("/internal/products/detail/{productId}")
    ProductSupplyDetailDTO queryProductDetailSupply(@PathVariable("productId") Long productId);

    @GetMapping("/internal/products/maps")
    List<ProductSupplyDTO> querySupplyMapForList(@RequestParam(name = "lng",required = true) Double lng,@RequestParam(name = "lat",required = true) Double lat,@RequestParam(name = "type") Integer type,@RequestParam(required = false,name = "userId") Long userId);

    @GetMapping("/internal/products/count")
    ProductHomeCountDTO queryProductHomeCount();

    @GetMapping("/internal/products/record/detail/{txn}")
    ProductSupplyDetailDTO queryProductDetailSupplyByTxnId(@PathVariable("txn") String txnId);
}
