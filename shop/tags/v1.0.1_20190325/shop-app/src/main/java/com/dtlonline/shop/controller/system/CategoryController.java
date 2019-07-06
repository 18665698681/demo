package com.dtlonline.shop.controller.system;

import com.dtlonline.api.shop.command.CategoryPackDTO;
import com.dtlonline.api.shop.command.CategoryParamDTO;
import com.dtlonline.shop.service.CategoryService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("systemCategoryController")
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
    public RestResult queryCategoryList(@RequestParam(defaultValue = "1") Integer type) {
        return RestResult.success(categoryService.queryCategoryList(type));
    }

    /**
     * 发布品类
     */
    @PostMapping(value = "/categorys", headers = "Authorization")
    public RestResult saveCategory(@RequestBody @Valid CategoryPackDTO categoryPackDTO, @RequestHeader("Authorization") String jwt) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("[{}],创建品类信息[{}]", userId, JsonUtils.toJSON(categoryPackDTO));
        categoryService.saveCategory(categoryPackDTO, userId);
        return RestResult.success();
    }

    /**
     * 编辑品类
     */
    @PutMapping(value = "/categorys/{id}", headers = "Authorization")
    public RestResult updateCategoryForId(@PathVariable("id") Long id, @RequestBody CategoryParamDTO categoryParamDTO,@RequestHeader("Authorization")String jwt) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("[{}],修改品类信息[{}]", userId, JsonUtils.toJSON(categoryParamDTO));
        categoryService.updateCategoryForId(id, categoryParamDTO,userId);
        return RestResult.success();
    }

    /**
     * 品类详情
     */
    @GetMapping("/categorys/{id}")
    public RestResult queryCategoryForId(@PathVariable("id") Long id) {
        return RestResult.success(categoryService.queryCategoryForId(id));
    }
}
