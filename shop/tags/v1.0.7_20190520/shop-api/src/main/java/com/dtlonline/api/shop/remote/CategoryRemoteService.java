package com.dtlonline.api.shop.remote;

import com.dtlonline.api.shop.view.CategoryAttentionDTO;
import com.dtlonline.api.shop.view.CategoryDTOI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${apps.shop.name:shop-app}",contextId = "CategoryRemoteService")
public interface CategoryRemoteService {

    /**
     * 关注列表
     */
    @GetMapping("/internal/categorys")
    List<CategoryAttentionDTO> queryCategoryAttention(@RequestParam("userId")Long userId);

    @GetMapping("/internal/categorys/{categoryId}")
    CategoryDTOI queryCategorysById(@PathVariable("categoryId")Long categoryId);
}
