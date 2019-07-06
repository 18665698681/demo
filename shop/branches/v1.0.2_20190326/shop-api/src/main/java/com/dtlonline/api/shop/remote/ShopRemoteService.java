package com.dtlonline.api.shop.remote;

import com.dtlonline.api.shop.view.ShopDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "${apps.shop.name:shop-app}",contextId = "ShopRemoteService")
public interface ShopRemoteService {
    /**
     * 根据userId查询商铺状态(目前只返回用户通过验证后的第一个店铺的状态)
     *
     * @param userId the user id
     * @return the boolean
     */
    @GetMapping("/internal/store/status/{userId}")
    Boolean queryShopStatus(@PathVariable("userId") Long userId);

    /**
     * 根据userId查询商品审核记录(目前只返回用户申请的最后一条审核单的状态)
     *
     * @param userId the user id
     * @return the integer
     */
    @GetMapping(value = "/internal/store/auth/status/{userId}")
    Integer queryShopAuthRecord(@PathVariable("userId") Long userId);

    /**
     * 根据商铺ID查询商铺
     *
     * @param id the id
     * @return the shop dto
     */
    @GetMapping(value = "/internal/store/{id}")
    ShopDTO queryShopById(@PathVariable("id") Long id);

    /**
     * 查询店铺信息
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/internal/store/list")
    Map<Long, ShopDTO> queryShopByIds(@RequestParam("id") Set<Long> ids);

    /**
     * 根据用户编号查询店铺信息
     * @param userId
     * @return
     */
    @GetMapping(value = "/internal/store/users/{userId}")
    ShopDTO queryShopByUserId(@PathVariable("userId") Long userId);
}
