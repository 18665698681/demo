package com.dtlonline.api.shop.remote.store;

import com.dtlonline.api.shop.command.store.StoreRepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient(value = "${apps.shop.name:shop-app}", contextId = "StoreRemoteService")
public interface StoreRemoteService {

    @GetMapping(value = "/internal/stores/list")
    List<StoreRepDTO> queryStoreList();

    @GetMapping("/internal/stores/list/ids")
    Map<Long, StoreRepDTO> queryStoreListById(@RequestParam("ids") Set<Long> ids);
}