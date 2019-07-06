package com.dtlonline.shop.controller.personal;

import com.dtlonline.api.shop.command.ProductQuoteDTO;
import com.dtlonline.shop.service.ProductQuoteService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("/personalProductQuoteController")
@RequestMapping("/personal")
public class ProductQuoteController extends BaseController {

    @Autowired
    private ProductQuoteService productQuoteService;

    @Autowired
    private JWTService jwtService;

    /**
     * 采购报价
     */
    @PostMapping(value = "/quotes", headers = "Authorization")
    public RestResult saveProductQuote(@RequestBody @Valid ProductQuoteDTO productQuoteDTO, @RequestHeader(name = "Authorization") String jwt) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("用户[{}],进行报价:[{}]", userId, JsonUtils.toJSON(productQuoteDTO));
        productQuoteService.saveProductQuote(productQuoteDTO, userId);
        return RestResult.success();
    }
}
