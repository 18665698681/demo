package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.api.shop.command.AuctionUserBuyer;
import com.dtlonline.api.shop.command.AuctionUserInto;
import com.dtlonline.shop.service.auction.AuctionCenterService;
import io.alpha.core.base.BaseController;
import io.alpha.core.service.JWTService;
import io.alpha.core.view.RestResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("personalAuctionCenterController")
@RequestMapping("/personal/auction")
public class AuctionCenterController extends BaseController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuctionCenterService auctionCenterService;


    /**
     * @Author gaoqiang
     * @Description 查询平台竞拍列表
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/list/platform")
    public RestResult list(@RequestParam(defaultValue = "1", required = false) Integer current,
                                @RequestParam(defaultValue = "10", required = false) Integer size,
                                @RequestParam(defaultValue = "2", required = false) Integer process) {
        return RestResult.success(auctionCenterService.queryAuctionListByPlatform(current,size,process));
    }

    /**
     * @Author gaoqiang
     * @Description 查询平台置顶竞拍商品
     * @Date 10:30 2019/3/8
     * @param process 1未开始 2竞拍中
     **/
    @GetMapping(value = "/top/platform")
    public RestResult top(@RequestParam(defaultValue = "2", required = false) Integer process) {
        return RestResult.success(auctionCenterService.queryAuctionTopByPlatform(process));
    }


    /**
     * @Author gaoqiang
     * @Description 根据productId查询详情
     * @Date 8:56 2019/3/9
     **/
    @GetMapping(value = "/detail/{productId}")
    public RestResult auctionDetails(@RequestHeader(name = "Authorization",required = false) String jwt,@PathVariable Long productId) {
        Long userId = 0L;
        if (StringUtils.isNotBlank(jwt)){
            userId = jwtService.getUserId(jwt);
        }
        return RestResult.success(auctionCenterService.queryAuctionDetails(userId,productId));
    }



    /**
     * @Author gaoqiang
     * @Description 出价记录
     * @Date 10:14 2019/3/9
     **/
    @GetMapping(value = "/user/record")
    public RestResult auctionRecords(@RequestParam(defaultValue = "1", required = false) Integer current,
                                     @RequestParam(defaultValue = "10", required = false) Integer size,
                                     @RequestParam(defaultValue = "",required = false) Long productId) {
        return RestResult.success(auctionCenterService.queryAuctionRecords(current,size,productId));
    }

    /**
     * @Author gaoqiang
     * @Description 用户申请参加竞拍
     * @Date 10:21 2019/3/9
     **/
    @PostMapping(value = "/user/into", headers = "Authorization")
    public RestResult userInto(@RequestHeader(name = "Authorization") String jwt, @RequestBody AuctionUserInto userInto) {
        Long userId = jwtService.getUserId(jwt);
        auctionCenterService.userInto(userId, userInto);
        return RestResult.success();
    }

    /**
     * @Author gaoqiang
     * @Description 当前用户出价的最高价信息
     * @Date 10:14 2019/3/9
     **/
    @GetMapping(value = "/user/max",headers = "Authorization")
    public RestResult userPrice(@RequestHeader(name = "Authorization") String jwt,@RequestParam Long productId) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionCenterService.queryUserMaxPrice(userId,productId));
    }

    /**
     * @Author gaoqiang
     * @Description 用户出价
     * @Date 11:33 2019/3/9
     **/
    @PostMapping(value = "/user/buyer", headers = "Authorization")
    public RestResult auctionBuyer(@RequestHeader(name = "Authorization") String jwt, @RequestBody AuctionUserBuyer buyer) {
        Long userId = jwtService.getUserId(jwt);
        auctionCenterService.auctionBuyer(userId, buyer);
        return RestResult.success();
    }

    /**
     * @Author gaoqiang
     * @Description 定时任务产生竞拍订单
     * @Date 10:11 2019/3/11
     **/
    @RequestMapping(value = "/task/order", method = RequestMethod.GET)
    public RestResult taskCreationOrder() {
        auctionCenterService.taskCreationOrder();
        return RestResult.success();
    }

    /**
     * @Author gaoqiang
     * @Description 查询用户参与的竞拍列表
     * @Date 10:30 2019/3/8
     * @param  process 1竞拍中 2竞拍成功 3竞拍失败 4全部
     **/
    @GetMapping(value = "/user/list", headers = "Authorization")
    public RestResult list(@RequestHeader(name = "Authorization") String jwt,
                           @RequestParam(defaultValue = "1", required = false) Integer current,
                           @RequestParam(defaultValue = "10", required = false) Integer size,
                           @RequestParam(defaultValue = "1", required = false) Integer process) {
        Long userId = jwtService.getUserId(jwt);
        return RestResult.success(auctionCenterService.queryAuctionListByUser(userId,current,size,process));
    }



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
        return RestResult.success(auctionCenterService.queryAuctionListBySeller(userId,current,size));
    }

    /**
     * @Author gaoqiang
     * @Description 卖家查询详情
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/seller/detail/{productId}", headers = "Authorization")
    public RestResult sellerDetail(@PathVariable Long productId) {
        return RestResult.success(auctionCenterService.querySellerDetail(productId));
    }
}
