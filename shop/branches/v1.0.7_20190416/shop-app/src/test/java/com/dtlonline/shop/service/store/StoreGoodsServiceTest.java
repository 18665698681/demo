package com.dtlonline.shop.service.store;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.store.StoreGoodsDTOI;
import com.dtlonline.api.shop.command.store.StoreGoodsPageDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsMergeQuantityDTO;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.UUID;

import com.dtlonline.api.shop.command.ProductRecordDTO;

import com.dtlonline.api.shop.command.store.StoreGoodsAuthRecordDTOI;
import io.alpha.app.core.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreGoodsServiceTest {

    @Autowired
    private StoreGoodsService storeGoodsService;
    @Autowired
    private StoreGoodsSplitService storeGoodsSplitService;

    @Test
    public void storeApply() {
        List<ProductStandardRecordDTO> standardList = new ArrayList<>();
        ProductStandardRecordDTO e1 = new ProductStandardRecordDTO();
        e1.setProductName("为什么要商品名");
        e1.setCategoryId(287L);
        e1.setStandardId(10L);
        e1.setStandardName("规格名？？？");
        e1.setData("一级");

        ProductStandardRecordDTO e2 = new ProductStandardRecordDTO();
        e2.setProductName("为什么要商品名");
        e2.setCategoryId(287L);
        e2.setStandardId(11L);
        e2.setStandardName("规格名？？？");
        e2.setData("国外");

        standardList.add(e1);
        standardList.add(e2);

        ProductRecordDTO productRecordDTO = new ProductRecordDTO();
        productRecordDTO.setTxnId("1223");
        productRecordDTO.setTitle("好商品");
        productRecordDTO.setType(3);
        productRecordDTO.setCategoryId(24L);
        productRecordDTO.setCategoryName("红片糖");
        productRecordDTO.setShopId(81L);
        productRecordDTO.setUnitPrice(new BigDecimal("33.5"));
        productRecordDTO.setStock(905);
        productRecordDTO.setMinDeal(30);
        productRecordDTO.setDescription("全世界最好的商品");
        productRecordDTO.setShowPrice(1);
        productRecordDTO.setProductionYear("2018");
        productRecordDTO.setInvoice(1);
        productRecordDTO.setProvince("广东省");
        productRecordDTO.setCity("东莞市");
        productRecordDTO.setArea("南城区");
        productRecordDTO.setAddress("");
        productRecordDTO.setStatus(1);
        productRecordDTO.setImgs("http://b.hiphotos.baidu.com/image/pic/item/4b90f603738da977f86d8b56be51f8198618e309.jpg");
        productRecordDTO.setExpireTime(LocalDate.now());
        productRecordDTO.setImageList(null);
        productRecordDTO.setStandardList(standardList);
        productRecordDTO.setAuctionApply(null);


      StoreGoodsAuthRecordDTOI test = new StoreGoodsAuthRecordDTOI();
        test.setTxnId(UUID.randomUUID().toString().replace("-", ""));
        test.setStoreId(3L);
        test.setQuantity(800);
        test.setInStoreDate(LocalDate.now());
        test.setCarNumber("粤B776655");
        test.setInsuranceMoney(new BigDecimal("55000"));
        test.setInsuranceBeginDate(LocalDate.now().minusDays(30));
        test.setInsuranceEndDate(LocalDate.now().plusDays(60));
        test.setProductRecordDTO(productRecordDTO);
        storeGoodsService.storeApply(test,81L);
    }

    @Test
    public void queryNotPassList() {
        StoreGoodsDTOI test1 = new StoreGoodsDTOI();
        test1.setCurrent(1);
        test1.setSize(10);
        test1.setUserId(81L);
        //IPage<StoreGoodsAuthRecordDTO> result = storeGoodsService.queryNotPassList(test1);
        //System.out.println(JsonUtils.toJSON(result.getRecords()));
    }

    @Test
    public void queryPassList() {
        StoreGoodsDTOI test = new StoreGoodsDTOI();
        test.setCurrent(1);
        test.setSize(10);
        //IPage<StoreGoodsDTO> result = storeGoodsService.queryPassList(test);
        //System.out.println(JsonUtils.toJSON(result.getRecords()));
    }

    @Test
    public void queryPassListMergeByStandard() {
        StoreGoodsPageDTOI test = new StoreGoodsPageDTOI();
        test.setCurrent(1);
        test.setSize(10);
        Page<StoreGoodsMergeQuantityDTO> storeGoodsMergeQuantityDTOPage = storeGoodsService.queryFormalListMergeByStandard(1, 20, 81L);
        System.out.println(JsonUtils.toJSON(storeGoodsMergeQuantityDTOPage.getRecords()));
    }


    @Test
    public void splitStoreGoods() {
        String newTxnId = UUID.randomUUID().toString().replaceAll("-", "");
        storeGoodsSplitService.split(1L, 3, 125, 20L, newTxnId);

    }

    @Test
    public void closeStoreGoodsAndReturnSplit() {
        storeGoodsSplitService.refuseAndReturnSplit(4L);
    }
}