package com.dtlonline.shop.service.groupbuy;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordDetailRequestDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRecordRequestDTO;
import io.alpha.app.core.util.SequenceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductGroupbuyRecordServiceTest {

    @Autowired
    private ProductGroupbuyRecordService productGroupbuyRecordService;
    @Test
    public void issueProductGroupbuyRecord() {
        List<ProductStandardRecordDTO> standardList = new ArrayList<>();
        standardList.add(new ProductStandardRecordDTO().setStandardId(1L).setStandardName("品牌").setData("江西"));
        standardList.add(new ProductStandardRecordDTO().setStandardId(2L).setStandardName("规格").setData("25KG/袋"));
        standardList.add(new ProductStandardRecordDTO().setStandardId(3L).setStandardName("品级").setData("精制"));
        standardList.add(new ProductStandardRecordDTO().setStandardId(4L).setStandardName("产地").setData("广西"));
        standardList.add(new ProductStandardRecordDTO().setStandardId(5L).setStandardName("性质").setData("甘蔗糖"));
        standardList.add(new ProductStandardRecordDTO().setStandardId(6L).setStandardName("区域").setData("国外"));

        ProductGroupbuyRecordRequestDTO dto = new ProductGroupbuyRecordRequestDTO();
        dto.setTxnId(SequenceUtils.getSequence());
        dto.setBuyerContractIds("1");
        dto.setProductGroupbuyId(31L);

        dto.setProductiveYear(2018);
        List<ProductGroupbuyRecordDetailRequestDTO> groupbuyRecordDetailList = new ArrayList<>();
        ProductGroupbuyRecordDetailRequestDTO request1 = new ProductGroupbuyRecordDetailRequestDTO();
        request1.setAddressId(147L).setCategoryId(18L).setDeliveryData("[{\"deliveryDate\":\"2019-05-30\",\"delivUserShippingAddressController.javaeryQuantity\":\"33\"},{\"deliveryDate\":\"2019-05-30\",\"deliveryQuantity\":\"3\"}]").setMinDeliveryQuantity(5).setQuantity(100).setStandardList(standardList);
        ProductGroupbuyRecordDetailRequestDTO request2 = new ProductGroupbuyRecordDetailRequestDTO();
        request2.setAddressId(147L).setCategoryId(18L).setDeliveryData("[{\"deliveryDate\":\"2019-05-30\",\"deliveryQuantity\":\"33\"},{\"deliveryDate\":\"2019-05-30\",\"deliveryQuantity\":\"3\"}]").setMinDeliveryQuantity(5).setQuantity(150).setStandardList(standardList);
        groupbuyRecordDetailList.add(request1);
        groupbuyRecordDetailList.add(request2);
        dto.setGroupbuyRecordDetailList(groupbuyRecordDetailList);
        productGroupbuyRecordService.issueProductGroupbuyRecord(dto,10114L);
    }
}