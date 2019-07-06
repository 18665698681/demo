package com.dtlonline.shop.controller.personal;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.ShopAuditDTO;
import com.dtlonline.shop.service.ShopService;
import com.dtlonline.shop.view.ShopViewCode;
import com.dtlonline.shop.view.ShopWithHeaderDTO;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController("personalShopController")
@RequestMapping("/personal")
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    /**
     * 申请店铺
     */
    @PostMapping(value = "/store")
    public RestResult applyForShop(@RequestHeader("Authorization") String jwt
            , @Valid @RequestBody ShopAuditDTO shopAuditDTO) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("[{}] 用户 [{}] 申请商铺 -> {}", shopAuditDTO.getTxnId(), userId, JsonUtils.toJSON(shopAuditDTO));
        shopService.saveAuditOperation(shopAuditDTO, userId);
        return RestResult.success();
    }

    /**
     * 店铺详情
     */
    @GetMapping(value = "/store/{id}")
    public RestResult queryShopById(@PathVariable("id") Long id) {
        try {
            return RestResult.success(shopService.queryShopWithStatusDTO(id));
        } catch (Exception e) {
            return RestResult.failure(ShopViewCode.NOT_EXIST_THIS_ID.getCode(), e.getMessage());
        }
    }

    /**
     * 店铺信息
     */
    @GetMapping(value = "/store")
    public RestResult queryShopId(@RequestHeader("Authorization") String jwt) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(shopService.queryShopByUserId(userId));
    }

    /**
     * 店铺审核详情
     */
    @GetMapping(value = "/store/record/{id}")
    public RestResult queryAuditById(@PathVariable("id") Long id) {
        return RestResult.success(shopService.queryAuditOperationById(id));
    }

    /**
     * 店铺头像
     */
    @PutMapping(value = "/store/headerId")
    public RestResult updateStoreHeader(@Valid @RequestBody ShopWithHeaderDTO shopWithHeaderDTO) {
        List<Long> imageList = imageRemoteService.batchSaveImage(Collections.singletonList(shopWithHeaderDTO.getHeaderImage())).getData();
        if (CollectionUtils.isNotEmpty(imageList)) {
            shopService.updateShopHeaderImgage(shopWithHeaderDTO.getShopId(), imageList.get(0));
            return RestResult.success();
        }
        return RestResult.failure();
    }
}
