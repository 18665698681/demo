package com.dtlonline.shop.service;

import com.dtlonline.api.isp.command.ImageDTO;
import com.dtlonline.shop.view.ProductDetailDTO;
import io.alpha.app.core.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void selectBuyerPrivileges() throws Exception {
        ProductDetailDTO productDetailDTO = productService.queryProductDetail(11L, 14L);
        System.out.println(JsonUtils.toJSON(productDetailDTO));
    }


}

