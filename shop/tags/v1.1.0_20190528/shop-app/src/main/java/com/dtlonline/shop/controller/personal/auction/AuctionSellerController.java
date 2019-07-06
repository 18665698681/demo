package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.api.shop.command.AuctionSellerPrice;
import com.dtlonline.shop.service.auction.AuctionCenterService;
import com.dtlonline.shop.service.auction.AuctionQueryService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
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

    @Autowired
    private AuctionCenterService auctionCenterService;

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
    @GetMapping(value = "/seller/detail/{id}", headers = "Authorization")
    public RestResult sellerDetail(@RequestHeader(name = "Authorization") String jwt,@PathVariable Long id) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionQueryService.querySellerDetail(userId,id));
    }

    /**
     * @Author gaoqiang
     * @Description 卖家修改价格
     * @Date 10:02 2019/3/30
     **/
    @PostMapping(value = "/seller/price", headers = "Authorization")
    public RestResult auctionBuyer(@RequestHeader(name = "Authorization") String jwt, @RequestBody AuctionSellerPrice sellerPrice) {
        Long userId = jwtService.getUserId(jwt);
        logger.info(" 卖家 [{}] 修改价格 -> {}",  userId, JsonUtils.toJSON(sellerPrice));
        auctionCenterService.updateSellerPrice(userId, sellerPrice);
        return RestResult.success();
    }

}
