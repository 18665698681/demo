package com.dtlonline.shop.controller.system;

import com.dtlonline.api.shop.command.CategoryPackDTO;
import com.dtlonline.api.shop.command.CategoryParamDTO;
import com.dtlonline.shop.service.CategoryService;
import io.alpha.core.base.BaseController;
import io.alpha.core.service.JWTService;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("SystemCategoryController")
@RequestMapping("/system")
public class CategoryController extends BaseController {

    @Autowired
    public CategoryService categoryService;

    @Autowired
    public JWTService jwtService;

    /**
     * 品类列表
     */
    @GetMapping("/categorys")
    public RestResult queryCategoryList(@RequestParam(defaultValue = "1") Integer type){
        return RestResult.success(categoryService.queryCategoryList(type));
    }

    /**
     * 发布品类
     */
    @PostMapping(value = "/categorys",headers = "Authorization")
    public RestResult saveCategory(@RequestBody @Valid CategoryPackDTO categoryPackDTO, @RequestHeader("Authorization") String jwt){
        Long userId = jwtService.getUserId(jwt);
        categoryService.saveCategory(categoryPackDTO,userId);
        return RestResult.success();
    }

    @PutMapping("/categorys/{id}")
    public RestResult updateCategoryForId(@PathVariable("id") Long id,@RequestBody CategoryParamDTO categoryParamDTO){
        categoryService.updateCategoryForId(id,categoryParamDTO);
        return RestResult.success();
    }
}
