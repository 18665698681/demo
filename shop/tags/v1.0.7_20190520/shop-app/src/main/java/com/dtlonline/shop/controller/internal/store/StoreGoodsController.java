package com.dtlonline.shop.controller.internal.store;

import com.dtlonline.api.shop.remote.store.StoreGoodsRemoteService;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.shop.service.store.StoreGoodsService;
import com.dtlonline.shop.service.store.StoreGoodsSplitService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/internal")
public class StoreGoodsController extends BaseController implements StoreGoodsRemoteService{

    @Autowired
    private StoreGoodsService storeGoodsService;
    @Autowired
    private StoreGoodsSplitService storeGoodsSplitService;

    @Override
    @PutMapping("/store/goods/refuse")
    public RestResult<Void> refuseStoreGoodsAndReturnSplit(Long storeGoodsId){
        storeGoodsSplitService.refuseAndReturnSplit(storeGoodsId);
        return RestResult.success();
    }

    @Override
    @PutMapping("/store/goods/pass")
    public RestResult<Void> passStoreGoods(Long storeGoodsId){
        storeGoodsSplitService.pass(storeGoodsId);
        return RestResult.success();
    }

    @Override
    @PutMapping("/store/goods/split")
    public RestResult<Long> splitStoreGoods(Long storeGoodsId, Integer bizType, Integer splitQuantity, Long newOwnerUserId, String newTxnId){
        Long newStoreGoodsId = storeGoodsSplitService.split(storeGoodsId, bizType, splitQuantity, newOwnerUserId, newTxnId);
        return RestResult.success(newStoreGoodsId);
    }

    @GetMapping("/stores/goods/ids")
    @Override
    public Map<Long, StoreGoodsDTO> queryStoreGoodsListById(@RequestParam("ids") Set<Long> ids) {
        return storeGoodsService.queryStoreGoodsListById(ids);
    }

    @GetMapping("/stores/goods/id/{goodsId}")
    @Override
    public StoreGoodsDTO queryStoreGoodsById(@PathVariable("goodsId") Long goodsId) {
        return storeGoodsService.queryStoreGoodsById(goodsId);
    }
}
