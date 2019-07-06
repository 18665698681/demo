package com.dtlonline.shop.service.auction;

import com.dtlonline.api.shop.command.AuctionUserBuyer;
import io.alpha.app.core.util.SequenceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuctionCenterServiceTest {

    @Autowired
    private  AuctionCenterService auctionCenterService;
    @Test
    public void auctionBuyer() {
        AuctionUserBuyer buyer = new AuctionUserBuyer();
        buyer.setTxnId(SequenceUtils.getSequence());
        buyer.setPrice(BigDecimal.valueOf(1000));
        buyer.setProductId(1153L);
        buyer.setQuantity(20);
        auctionCenterService.auctionBuyer(10054L,buyer);
    }
}