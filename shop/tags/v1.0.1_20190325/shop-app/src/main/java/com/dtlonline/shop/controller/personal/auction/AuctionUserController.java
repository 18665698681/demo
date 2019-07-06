package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.api.shop.command.AuctionUserBuyer;
import com.dtlonline.api.shop.command.AuctionUserInto;
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
 * @Description 买家相关接口
 * @Date 14:36 2019/3/18
 **/

@RestController("personalAuctionUserController")
@RequestMapping("/personal/auction")
public class AuctionUserController extends BaseController {
    @Autowired
    JWTService jwtService;

    @Autowired
    private AuctionCenterService auctionCenterService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    /**
     * @Author gaoqiang
     * @Description 出价记录
     * @Date 10:14 2019/3/9
     **/
    @GetMapping(value = "/user/record")
    public RestResult auctionRecords(@RequestParam(defaultValue = "1", required = false) Integer current,
                                     @RequestParam(defaultValue = "10", required = false) Integer size,
                                     @RequestParam(defaultValue = "", required = false) Long productId) {
        return RestResult.success(auctionQueryService.queryAuctionRecords(current, size, productId));
    }

    /**
     * @Author gaoqiang
     * @Description 用户申请参加竞拍
     * @Date 10:21 2019/3/9
     **/
    @PostMapping(value = "/user/into", headers = "Authorization")
    public RestResult userInto(@RequestHeader(name = "Authorization") String jwt, @RequestBody AuctionUserInto userInto) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("[{}] 买家 [{}] 参与竞拍 -> {}", userInto.getTxnId(), userId, JsonUtils.toJSON(userInto));
        auctionCenterService.userInto(userId, userInto);
        return RestResult.success();
    }

    /**
     * @Author gaoqiang
     * @Description 用户申请参加竞拍前检查
     * @Date 10:21 2019/3/9
     **/
    @GetMapping(value = "/user/check/{productId}", headers = "Authorization")
    public RestResult userInto(@RequestHeader(name = "Authorization") String jwt, @PathVariable Long productId) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionCenterService.userIntoCheck(userId, productId));
    }

    /**
     * @Author gaoqiang
     * @Description 当前用户出价的最高价信息
     * @Date 10:14 2019/3/9
     **/
    @GetMapping(value = "/user/max", headers = "Authorization")
    public RestResult userPrice(@RequestHeader(name = "Authorization") String jwt, @RequestParam Long productId) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionQueryService.queryUserMaxPrice(userId, productId));
    }

    /**
     * @Author gaoqiang
     * @Description 用户出价
     * @Date 11:33 2019/3/9
     **/
    @PostMapping(value = "/user/buyer", headers = "Authorization")
    public RestResult auctionBuyer(@RequestHeader(name = "Authorization") String jwt, @RequestBody AuctionUserBuyer buyer) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("[{}] 买家 [{}] 出价 -> {}", buyer.getTxnId(), userId, JsonUtils.toJSON(buyer));
        auctionCenterService.auctionBuyer(userId, buyer);
        return RestResult.success();
    }

    /**
     * @param process -1参与的竞拍 1竞拍中 2竞拍成功 3竞拍结束
     * @Author gaoqiang
     * @Description 查询用户参与的竞拍列表
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/user/list", headers = "Authorization")
    public RestResult list(@RequestHeader(name = "Authorization") String jwt,
                           @RequestParam(defaultValue = "1", required = false) Integer current,
                           @RequestParam(defaultValue = "10", required = false) Integer size,
                           @RequestParam(defaultValue = "1", required = false) Integer process) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionQueryService.queryAuctionListByUser(userId, current, size, process));
    }

}
