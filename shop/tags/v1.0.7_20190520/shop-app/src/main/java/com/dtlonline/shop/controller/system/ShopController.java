package com.dtlonline.shop.controller.system;

import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ShopAuthQueryPageDTO;
import com.dtlonline.shop.model.ShopAuthRecord;
import com.dtlonline.shop.service.ShopService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("systemShopController")
@RequestMapping("/system")
public class ShopController extends BaseController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private JWTService jwtService;

    /**
     * 审核店铺
     */
    @PutMapping(value = "/shops/approval")
    public RestResult checkShopInfo(@RequestHeader("Authorization") String jwt, @Valid @RequestBody CheckInfoDTO checkInfoDTO) {
        String staffName = jwtService.getUserName(jwt);
        logger.info("用户 [{}] 审核店铺 -> [{}]", staffName, JsonUtils.toJSON(checkInfoDTO));
        shopService.checkShopAudit(staffName, checkInfoDTO);
        return RestResult.success();
    }

    /**
     * 店铺审核列表
     */
    @GetMapping(value = "/shops")
    public RestResult queryShopPage(ShopAuthQueryPageDTO<ShopAuthRecord> shopShopAuthQueryPageDTO) {
        return RestResult.success(shopService.queryShopAuthRecordsPage(shopShopAuthQueryPageDTO));
    }

    /**
     * 审核详情
     */
    @GetMapping(value = "/shops/{id}")
    public RestResult queryShopDetail(@PathVariable("id") Long id) {
        return RestResult.success(shopService.queryAuditOperationById(id));
    }
}
