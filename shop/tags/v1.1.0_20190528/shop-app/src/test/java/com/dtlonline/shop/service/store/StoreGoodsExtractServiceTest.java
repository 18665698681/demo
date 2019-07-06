package com.dtlonline.shop.service.store;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.store.StoreGoodsExtractDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsExtractDTO;
import io.alpha.app.core.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreGoodsExtractServiceTest {

    @Autowired
    private StoreGoodsExtractService storeGoodsExtractService;

    @Test
    public void extractApply() {
        StoreGoodsExtractDTOI storeGoodsExtractDTOI = new StoreGoodsExtractDTOI();
        storeGoodsExtractDTOI.setTxnId(UUID.randomUUID().toString().replaceAll("-", ""));
        storeGoodsExtractDTOI.setQuantity(23);
        //storeGoodsExtractDTOI.setUserId(18L);
        storeGoodsExtractDTOI.setStoreGoodsId(4L);
        storeGoodsExtractDTOI.setAddressId(16L);
        //storeGoodsExtractDTOI.setLogisticsCosts(new BigDecimal("0"));
        storeGoodsExtractDTOI.setPapersType(1);
        storeGoodsExtractDTOI.setPapersNumber("");

        storeGoodsExtractService.extractApply(storeGoodsExtractDTOI,222L);;
    }

    @Test
    public void queryList() {

        StoreGoodsExtractDTOI storeGoodsExtractDTOI = new StoreGoodsExtractDTOI();
/*        storeGoodsExtractDTOI.setCurrent(1);
        storeGoodsExtractDTOI.setSize(3);*/
        Page<StoreGoodsExtractDTO> storeGoodsExtractDTOPage = storeGoodsExtractService.queryList(1,2,3L);
        System.out.println(JsonUtils.toJSON(storeGoodsExtractDTOPage.getRecords()));
    }
}