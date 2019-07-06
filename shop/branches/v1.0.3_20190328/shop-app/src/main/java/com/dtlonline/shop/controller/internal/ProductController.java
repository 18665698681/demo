package com.dtlonline.shop.controller.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.remote.ProductRemoteService;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.ProductSupplyDetailDTO;
import com.dtlonline.shop.service.ProductService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController("internalProductController")
@RequestMapping("/internal")
public class ProductController extends BaseController implements ProductRemoteService {

    @Autowired
    public ProductService productService;

    /**
     * 商品详情
     */
    @GetMapping("/products/{productId}")
    @Override
    public ProductInfoDTO queryProductInfo(@PathVariable("productId") Long productId) {
        return productService.queryProductInfo(productId);
    }

    /**
     * 商品列表
     */
    @GetMapping("/products/list")
    @Override
    public Map<Long, ProductInfoDTO> queryProductInfo(@RequestParam("id") Set<Long> ids) {
        return productService.queryProductInfoByIds(ids);
    }

    /**
     * 商品列表(聚一搜货)
     */
    @Override
    @GetMapping("/products")
    public Page queryProductSupplyForList(Integer current,Integer size,Long userId,Long shopId){
        return productService.queryProductSupplyForList(current,size,userId,shopId);
    }

    /**
     * 商品详情(聚一搜货)
     */
    @Override
    @GetMapping("/products/detail/{productId}")
    public ProductSupplyDetailDTO queryProductDetailSupply(@PathVariable("productId") Long productId){
        return productService.queryProductDetailSupply(productId);
    }
}
