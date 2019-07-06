package com.dtlonline.shop.service.store;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.command.ImageDTO;
import com.dtlonline.api.shop.command.store.StoreGoodsRequireQueryDTOI;
import com.dtlonline.api.shop.view.store.StoreGoodsRequireDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dtlonline.api.shop.command.store.StoreGoodsRequireDTOI;
import io.alpha.app.core.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreGoodsRequireServiceTest {
    @Autowired
    private StoreGoodsRequireService storeGoodsRequireService;

    @Test
    public void publishMulti() {

        List<ImageDTO> imageDTOS = new ArrayList<>();
        ImageDTO image1 = new ImageDTO();
        ImageDTO image2 = new ImageDTO();
        imageDTOS.add(image1);
        imageDTOS.add(image2);
        image1.setUrl("/20190510/201905101028527125329000000167.png");
        image2.setUrl("/20190510/201905101028527125329000000167.png");
        image1.setName("good222");
        image2.setName("good1111");
        image1.setFormat("jpg");
        image2.setFormat("jpg");
        image1.setOriginalFileName("good222.jpg");
        image2.setOriginalFileName("good1111.jpg");
        image1.setPixel("232*300");
        image2.setPixel("232*300");
        image1.setSize("300");
        image2.setSize("200");

        StoreGoodsRequireDTOI storeGoodsRequire = new StoreGoodsRequireDTOI();
        storeGoodsRequire.setTxnId(UUID.randomUUID().toString().replaceAll("-", ""));
        storeGoodsRequire.setUserId(0L);
        storeGoodsRequire.setStoreGoodsId(4L);
        storeGoodsRequire.setTitle("");
        storeGoodsRequire.setTradeType(0);
        storeGoodsRequire.setQuantityLend(30);
        storeGoodsRequire.setOffSaleDate(LocalDate.now());
        storeGoodsRequire.setReturnRate(new BigDecimal("0"));
        storeGoodsRequire.setInsuranceMoney(new BigDecimal("0"));
        storeGoodsRequire.setInsuranceBeginDate(LocalDate.now());
        storeGoodsRequire.setInsuranceEndDate(LocalDate.now());
        storeGoodsRequire.setRequireTradeZone("广州");
        storeGoodsRequire.setAuditUser("");
        storeGoodsRequire.setAuditOpinion("eqeqeq");
        storeGoodsRequire.setAuditStatus(50);
        storeGoodsRequire.setOutProductImages(imageDTOS);
        storeGoodsRequire.setStoreAddress("地址");
        storeGoodsRequire.setStoreName("仓库名");

        storeGoodsRequireService.publishMulti(storeGoodsRequire,10005L);;
    }

    @Test
    public void queryList() {
        StoreGoodsRequireQueryDTOI good = new StoreGoodsRequireQueryDTOI();
        good.setCurrent(1);
        good.setSize(1000);
        Page<StoreGoodsRequireDTO> storeGoodsRequireDTOPage = storeGoodsRequireService.queryList(good);
        System.out.println(JsonUtils.toJSON(storeGoodsRequireDTOPage));

    }

    @Test
    public void publishOne() {
        
    }
}