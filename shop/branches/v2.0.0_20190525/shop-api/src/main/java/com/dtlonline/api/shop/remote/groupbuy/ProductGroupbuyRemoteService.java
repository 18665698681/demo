package com.dtlonline.api.shop.remote.groupbuy;

import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "ProductGroupbuyRemoteService")
public interface ProductGroupbuyRemoteService {

    /** 查询团购列表 */
    @GetMapping("/internal/products/groupbuy/list")
    RestResult<Page<ProductGroupbuyDTO>> queryList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "process", defaultValue = "2") Integer process);

    /** 查询仓单信息列表  根据ids*/
    @GetMapping("/internal/products/groupbuy/detail/{recordId}")
    RestResult<ProductGroupbuyDTO> queryDetails(@PathVariable("recordId") Long recordId);
}
