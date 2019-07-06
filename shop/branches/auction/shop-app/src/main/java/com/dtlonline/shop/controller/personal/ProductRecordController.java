package com.dtlonline.shop.controller.personal;

import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.shop.service.ProductRecordService;
import io.alpha.core.base.BaseController;
import io.alpha.core.service.JWTService;
import io.alpha.core.util.JsonUtils;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("personalProductRecordController")
@RequestMapping("personal")
public class ProductRecordController extends BaseController {
    @Autowired
    public ProductRecordService productRecordService;

    @Autowired
    public JWTService jwtService;

    @PostMapping(value = "/products", headers = "Authorization")
    public RestResult productRecordPublish(@Valid @RequestBody ProductRecordDTO productRecordDTO, @RequestHeader("Authorization") String jwt) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("用户[{}],上架商品,[{}]",userId,JsonUtils.toJSON(productRecordDTO));
        productRecordService.saveProductOtherExtra(productRecordDTO,userId);
        return RestResult.success();
    }
}
