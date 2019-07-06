package com.dtlonline.api.shop.remote.store;

import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "StoreGoodsRemoteService")
public interface StoreGoodsRemoteService {
    /**
     * 关闭某个仓单，并且归还数量到父仓单
     * @param storeGoodsId 仓单Id
     */
    @PutMapping("/internal/store/goods/refuse")
    RestResult<Void> refuseStoreGoodsAndReturnSplit(@RequestParam("storeGoodsId") Long storeGoodsId);

    /**
     * 正式启用某个仓单，不允许再归还数量
     * @param storeGoodsId 仓单Id
     */
    @PutMapping("/internal/store/goods/pass")
    RestResult<Void> passStoreGoods(@RequestParam("storeGoodsId") Long storeGoodsId);

    /**
     * 分裂某个仓单，使其变成两个仓单，为待审核状态
     * 新的仓单是临时的，审核不通过可以返回数量
     * @param storeGoodsId 原始仓单Id
     */
    @PutMapping("/internal/store/goods/split")
    RestResult<Long> splitStoreGoods(@RequestParam("storeGoodsId") Long storeGoodsId,@RequestParam("bizType") Integer bizType,@RequestParam("splitQuantity") Integer splitQuantity,@RequestParam("newOwnerUserId") Long newOwnerUserId,@RequestParam("newTxnId") String newTxnId);

    /** 查询仓单信息列表  根据ids*/
    @GetMapping("/internal/stores/goods/ids")
    Map<Long, StoreGoodsDTO> queryStoreGoodsListById(@RequestParam("ids") Set<Long> ids);

    /** 查询仓单信息根据 id */
    @GetMapping("/internal/stores/goods/id/{goodsId}")
    StoreGoodsDTO queryStoreGoodsById(@PathVariable("goodsId") Long goodsId);
}
