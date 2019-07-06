package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.shop.service.auction.AuctionQueryService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gaoqiang
 * @Description 卖家相关接口
 * @Date 14:36 2019/3/18
 **/

@RestController("personalAuctionSellerController")
@RequestMapping("/personal/auction")
public class AuctionSellerController extends BaseController {

    @Autowired
    JWTService jwtService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    /**
     * @Author gaoqiang
     * @Description 卖家发布的竞拍列表
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/seller/list", headers = "Authorization")
    public RestResult sellerList(@RequestHeader(name = "Authorization") String jwt,
                                 @RequestParam(defaultValue = "1", required = false) Integer current,
                                 @RequestParam(defaultValue = "10", required = false) Integer size) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionQueryService.queryAuctionListBySeller(userId, current, size));
    }

    /**
     * @Author gaoqiang
     * @Description 卖家查询详情
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/seller/detail/{productId}", headers = "Authorization")
    public RestResult sellerDetail(@PathVariable Long productId) {
        return RestResult.success(auctionQueryService.querySellerDetail(productId));
    }

}
