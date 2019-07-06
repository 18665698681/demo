package com.dtlonline.shop.controller.personal;

import com.dtlonline.shop.service.StandardService;
import io.alpha.core.base.BaseController;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("personalStandardController")
@RequestMapping("/personal")
public class StandardController extends BaseController {

    @Autowired
    public StandardService standardService;

    /**
     * 规格列表
     */
    @GetMapping("standards/{categoryId}")
    public RestResult queryStandardForCategory(@PathVariable("categoryId") Long categoryId){
        return RestResult.success(standardService.queryStandardForCategoryId(categoryId));
    }
}
