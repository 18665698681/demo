package com.dtlonline.shop.controller.system;

import com.dtlonline.shop.service.CategoryService;
import io.alpha.core.base.BaseController;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("SystemCategoryController")
@RequestMapping("/system")
public class CategoryController extends BaseController {

    @Autowired
    public CategoryService categoryService;

    /**
     * 品类列表
     */
    @GetMapping("/categorys")
    public RestResult categoryList(@RequestParam(defaultValue = "1") Integer type){
        return RestResult.success(categoryService.queryCategoryList(type));
    }
}
