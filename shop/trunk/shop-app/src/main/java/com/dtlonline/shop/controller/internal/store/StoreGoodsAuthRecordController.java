package com.dtlonline.shop.controller.internal.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsAuthRecordDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsDTOI;
import com.dtlonline.api.shop.remote.store.StoreGoodsAuthRecordRemoteService;
import com.dtlonline.api.shop.view.store.StoreGoodsAuthRecordDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsMergeQuantityDTO;
import com.dtlonline.shop.service.store.StoreGoodsService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("internalStoreGoodsAuthRecordController")
@RequestMapping("/internal")
public class StoreGoodsAuthRecordController extends BaseController implements StoreGoodsAuthRecordRemoteService {

    @Autowired
    private StoreGoodsService storeGoodsService;

    @Override
    @PostMapping("/stores")
    public void  issueStoreGoodsAuthRecord(@RequestBody StoreGoodsAuthRecordDTOI storeGoodsAuthRecord, Long userId){
        storeGoodsService.storeApply(storeGoodsAuthRecord,userId);
    }

    @Override
    @GetMapping("/stores/pass")
    public Page<StoreGoodsDTO> queryStoreGoodsAuthPassList(Integer current,Integer size ,Long userId,@RequestParam(name = "storeId",required = false)Long storeId,@RequestParam(name = "productTitle",required = false)String productTitle){
        StoreGoodsDTOI storeGoodsDTOI = new StoreGoodsDTOI();
        storeGoodsDTOI.setCurrent(current);
        storeGoodsDTOI.setSize(size);
        storeGoodsDTOI.setUserId(userId);
        storeGoodsDTOI.setStoreId(storeId);
        storeGoodsDTOI.setProductTitle(productTitle);
        return storeGoodsService.queryFormalList(storeGoodsDTOI);
    }

    @Override
    @GetMapping("/stores/record")
    public Page<StoreGoodsAuthRecordDTO> queryStoreGoodsAuthRecordList(Integer current,Integer size,Long userId){
        return storeGoodsService.queryAuditingList(current,size,userId);
    }

    @Override
    @GetMapping("/stores")
    public Page<StoreGoodsMergeQuantityDTO> queryMyStoreGoodsList(Integer current, Integer size, Long userId){
        return storeGoodsService.queryFormalListMergeByStandard(current,size,userId);
    }

    @Override
    @GetMapping("/stores/record/{goodsId}")
    public StoreGoodsAuthRecordDTO queryStoreGoodsAuthorDetailForId(@PathVariable("goodsId") Long goodsId){
        return storeGoodsService.queryAuditOne(goodsId);
    }

    @Override
    @GetMapping("/stores/{goodsId}")
    public StoreGoodsDTO queryStoreGoodsDetailForId(@PathVariable("goodsId") Long goodsId){
        return storeGoodsService.queryPassedOne(goodsId);
    }
}
