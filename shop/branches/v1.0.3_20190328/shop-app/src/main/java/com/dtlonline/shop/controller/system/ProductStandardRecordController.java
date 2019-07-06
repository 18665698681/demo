package com.dtlonline.shop.controller.system;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.shop.service.ProductStandardRecordService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("systemProductStandardRecordController")
@RequestMapping("system")
public class ProductStandardRecordController extends BaseController {

    @Autowired
    public ProductStandardRecordService productStandardRecordService;

    /**
     * 规格列表
     */
    @GetMapping("/standards")
    public RestResult queryProductStandardList(ProductStandardRecordDTO productStandardRecordDTO) {
        return RestResult.success(productStandardRecordService.queryProductStandardList(productStandardRecordDTO));
    }
}
