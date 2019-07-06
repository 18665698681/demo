package com.dtlonline.api.shop.remote.groupbuy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordRequestDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordListDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "ProductGroupbuyRecordRemoteService")
public interface ProductGroupbuyRecordRemoteService {

    @PostMapping("/internal/products/groupbuy/records")
    RestResult<Void> issueProductGroupbuyRecord(@RequestBody ProductGroupbuyRecordRequestDTO groupbuyRecord,@RequestParam("userId") Long userId);

    @GetMapping("/internal/products/groupbuy/records/registration")
    RestResult<Page<ProductGroupbuyRecordListDTO>> queryProductGroupbuyRecordForList(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("userId") Long userId,@RequestParam("process") Integer process);

    @GetMapping("/internal/products/groupbuy/records/published")
    RestResult<Page<ProductGroupbuyRecordListDTO>> queryGroupbuyRecordPublished(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("userId") Long userId);
}
