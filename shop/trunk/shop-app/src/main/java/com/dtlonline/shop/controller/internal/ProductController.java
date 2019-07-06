package com.dtlonline.shop.controller.internal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.remote.ProductRemoteService;
import com.dtlonline.api.shop.view.ProductHomeCountDTO;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.shop.view.ProductSupplyDTO;
import com.dtlonline.api.shop.view.ProductSupplyDetailDTO;
import com.dtlonline.shop.service.ProductService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
     * 商品搜索列表(聚一搜获)
     */
    @Override
    @GetMapping("/products")
    public Page queryProductSupplyForList(Integer current,Integer size,String keywords){
        return productService.queryProductSupplyForList(current,size,keywords);
    }

    /**
     * 商品详情(聚一搜获)
     */
    @Override
    @GetMapping("/products/detail/{productId}")
    public ProductSupplyDetailDTO queryProductDetailSupply(@PathVariable("productId") Long productId){
        return productService.queryProductDetailSupply(productId);
    }

    /**
     * 商品记录详情(聚一搜获)
     */
    @Override
    @GetMapping("/products/record/detail/{txn}")
    public ProductSupplyDetailDTO queryProductDetailSupplyByTxnId(@PathVariable("txn") String txnId){
        return productService.queryProductDetailSupplyByTxnId(txnId);
    }

    /**
     * 地图搜索列表
     */
    @Override
    @GetMapping("/products/maps")
    public List<ProductSupplyDTO> querySupplyMapForList(Double lng,Double lat,Integer type,Long userId){
        return productService.querySupplyMapForList(lng,lat,type,userId);
    }

    /**
     * 分类统计商品数量(聚一搜获)
     */
    @Override
    @GetMapping("/products/count")
    public ProductHomeCountDTO queryProductHomeCount(){
        return productService.queryProductHomeCount();
    }
}
