package com.dtlonline.shop.service.store;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.command.store.StoreRepDTO;
import com.dtlonline.shop.mapper.store.StoreMapper;
import com.dtlonline.shop.model.store.Store;
import com.google.common.collect.Maps;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class StoreService extends BaseService {

    @Autowired
    private StoreMapper storeMapper;

    /**
     * 查询仓库注册记录列表
     *
     * @return
     */
    public List<StoreRepDTO> queryStoreList() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", SUCCESS);
        List<Store> selectList = storeMapper.selectList(wrapper);
        List<StoreRepDTO> storeAuthRecordRepDTOS = new ArrayList<StoreRepDTO>();
        selectList.forEach(store -> {
            storeAuthRecordRepDTOS.add(Store.ofStoreAuthRecord(store));
        });
        return storeAuthRecordRepDTOS;
    }

    /**
     * 根据id查询仓库
     *
     * @param ids
     * @return
     */
    public Map<Long, StoreRepDTO> queryStore(Set<Long> ids) {
        Map<Long, StoreRepDTO> storeMap = Maps.newHashMap();
        ids.forEach(id -> storeMap.put(id, Store.ofStoreAuthRecord(storeMapper.selectById(id))));
        return storeMap;
    }
}