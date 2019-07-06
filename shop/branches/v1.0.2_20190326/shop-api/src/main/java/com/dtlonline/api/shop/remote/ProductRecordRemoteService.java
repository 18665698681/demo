package com.dtlonline.api.shop.remote;

import com.dtlonline.api.shop.command.ProductRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${apps.shop.name:shop-app}", contextId = "ProductRecordRemoteService")
public interface ProductRecordRemoteService {

    @PostMapping("/internal/products")
    void saveProductOtherExtra(@RequestBody ProductRecordDTO productRecordDTO, @RequestParam("userId") Long userId);
}
