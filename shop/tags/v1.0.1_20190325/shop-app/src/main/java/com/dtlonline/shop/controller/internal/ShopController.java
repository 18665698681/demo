package com.dtlonline.shop.controller.internal;

import com.dtlonline.api.shop.remote.ShopRemoteService;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.shop.service.ShopService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController("internalShopController")
@RequestMapping("/internal")
public class ShopController extends BaseController implements ShopRemoteService {

    @Autowired
    private ShopService shopService;

    /**
     * 店铺状态
     */
    @GetMapping(value = "/store/status/{userId}")
    @Override
    public Boolean queryShopStatus(@PathVariable("userId") Long userId) {
        return shopService.queryShopStatusByUserId(userId);
    }

    /**
     * 店铺审核状态
     */
    @GetMapping(value = "/store/auth/status/{userId}")
    @Override
    public Integer queryShopAuthRecord(@PathVariable("userId") Long userId) {
        return shopService.queryShopAuthRecordsByUserId(userId);
    }

    /**
     * 店铺详情
     */
    @GetMapping(value = "/store/{id}")
    @Override
    public ShopDTO queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 店铺列表
     */
    @GetMapping(value = "/store/list")
    @Override
    public Map<Long, ShopDTO> queryShopByIds(@RequestParam("id") Set<Long> ids) {
        return shopService.queryByIds(ids);
    }

    @GetMapping(value = "/store/users/{userId}")
    @Override
    public ShopDTO queryShopByUserId(@PathVariable Long userId) {
        return shopService.queryByUserId(userId);
    }
}
