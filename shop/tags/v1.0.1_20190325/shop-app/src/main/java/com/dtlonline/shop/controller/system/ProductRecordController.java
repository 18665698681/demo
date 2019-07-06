package com.dtlonline.shop.controller.system;

import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ProductDTO;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.shop.service.ProductRecordService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("systemProductRecordController")
@RequestMapping("/system")
public class ProductRecordController extends BaseController {

    @Autowired
    public ProductRecordService productRecordService;

    @Autowired
    public JWTService jwtService;

    /**
     * 商品审核列表
     */
    @GetMapping(value = "/products/list")
    public RestResult<ProductDTO> queryProductRecordList(ProductRecordDTO productRecordDTO) {
        return RestResult.success(productRecordService.queryProductRecordList(productRecordDTO));
    }

    /**
     * 审核商品详情
     */
    @GetMapping(value = "/products/{id}")
    public RestResult<ProductDTO> queryProductRecordDetail(@PathVariable("id") Long id) {
        return RestResult.success(productRecordService.queryProductRecordDetail(id));
    }

    /**
     * 审核商品
     */
    @PutMapping(value = "/approval", headers = "Authorization")
    public RestResult approval(@RequestBody CheckInfoDTO checkInfoDTO, @RequestHeader("Authorization") String jwt) {
        String recordName = jwtService.getUserName(jwt);
        productRecordService.approval(recordName, checkInfoDTO);
        return RestResult.success();
    }
}
