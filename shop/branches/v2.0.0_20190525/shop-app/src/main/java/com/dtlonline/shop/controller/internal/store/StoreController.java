package com.dtlonline.shop.controller.internal.store;

import com.dtlonline.api.shop.command.store.StoreRepDTO;
import com.dtlonline.api.shop.remote.store.StoreRemoteService;
import com.dtlonline.shop.service.store.StoreService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/internal")
public class StoreController extends BaseController implements StoreRemoteService {

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/stores/list")
    @Override
    public List<StoreRepDTO> queryStoreList() {
        return storeService.queryStoreList();
    }

    @GetMapping("/stores/list/ids")
    @Override
    public Map<Long, StoreRepDTO> queryStoreListById(@RequestParam("ids") Set<Long> ids) {
        return storeService.queryStore(ids);
    }
}