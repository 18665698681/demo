package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.api.shop.command.AuctionApply;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ProductDTO;
import com.dtlonline.shop.service.auction.AuctionTradeService;
import io.alpha.core.base.BaseController;
import io.alpha.core.service.JWTService;
import io.alpha.core.util.JsonUtils;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("personalAuctionTradeController")
@RequestMapping("/personal")
public class AuctionTradeController extends BaseController {
    @Autowired
    JWTService jwtService;

    @Autowired
    AuctionTradeService auctionTradeService;

}
