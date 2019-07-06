package com.dtlonline.shop.controller.system.auction;

import com.dtlonline.shop.service.auction.AuctionCenterService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("systemAuctionCenterController")
@RequestMapping("/system")
public class AuctionCenterController extends BaseController {

    @Autowired
    private AuctionCenterService auctionCenterService;

    /**
     * 竞拍审核列表
     */
    @GetMapping("/auctions")
    public RestResult queryProductAuctionLists(@RequestParam(defaultValue = "1") Integer current, @RequestParam(defaultValue = "10") Integer size, String title, Integer status) {
        return RestResult.success(auctionCenterService.queryProductAuctionLists(current, size, title, status));
    }

    /**
     * 竞拍详情
     */
    @GetMapping("/auctions/{tId}")
    public RestResult queryProductRecordForId(@PathVariable("tId") String tId) {
        return RestResult.success(auctionCenterService.queryProductRecordForId(tId));
    }
}
