package com.dtlonline.shop.controller.internal;

import com.dtlonline.api.shop.remote.CategoryRemoteService;
import com.dtlonline.api.shop.view.CategoryAttentionDTO;
import com.dtlonline.api.shop.view.CategoryDTOI;
import com.dtlonline.shop.service.CategoryService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("internalCategoryController")
@RequestMapping("/internal")
public class CategoryController extends BaseController implements CategoryRemoteService{

    @Autowired
    private CategoryService categoryService;

    @Override
    @GetMapping(value = "/categorys")
    public List<CategoryAttentionDTO> queryCategoryAttention(Long userId) {
        return categoryService.queryCategoryAttention(userId);
    }


    @Override
    @GetMapping(value = "/categorys/{categoryId}")
    public CategoryDTOI queryCategorysById(@PathVariable("categoryId") Long categoryId) {
        return categoryService.queryCategorysById(categoryId);
    }

    @Override
    @GetMapping("/category/list/{categoryId}")
    public RestResult<List<CategoryDTOI>> queryCategoryListSuggest(@PathVariable("categoryId") Long categoryId){
        return RestResult.success(categoryService.queryCategoryListSuggest(categoryId));
    }
}
