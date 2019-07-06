package com.dtlonline.shop.controller.personal.auction;

import com.dtlonline.shop.service.auction.AuctionQueryService;
import com.dtlonline.shop.service.task.AuctionTaskService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("personalAuctionCenterController")
@RequestMapping("/personal/auction")
public class AuctionCenterController extends BaseController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuctionTaskService auctionTaskService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    /**
     * @Author gaoqiang
     * @Description 查询平台竞拍列表
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/list/platform")
    public RestResult list(@RequestParam(defaultValue = "1", required = false) Integer current,
                           @RequestParam(defaultValue = "10", required = false) Integer size,
                           @RequestParam(defaultValue = "2", required = false) Integer process) {
        return RestResult.success(auctionQueryService.queryAuctionListByPlatform(current, size, process));
    }

    /**
     * @param process 1未开始 2竞拍中
     * @Author gaoqiang
     * @Description 查询平台置顶竞拍商品
     * @Date 10:30 2019/3/8
     **/
    @GetMapping(value = "/top/platform")
    public RestResult top(@RequestParam(defaultValue = "2", required = false) Integer process) {
        return RestResult.success(auctionQueryService.queryAuctionTopByPlatform(process));
    }

    /**
     * @Author gaoqiang
     * @Description 竞拍首页列表数据
     * @Date 14:48 2019/3/19
     **/
    @GetMapping(value = "/list/home")
    public RestResult listByHome() {
        return RestResult.success(auctionQueryService.queryAuctionListByHome());
    }

    /**
     * @Author gaoqiang
     * @Description 查询竞拍详情
     * @Date 8:56 2019/3/9
     **/
    @GetMapping(value = "/detail/{productId}")
    public RestResult auctionDetails(@RequestHeader(name = "Authorization", required = false) String jwt, @PathVariable Long productId) {
        Long userId = 0L;
        if (StringUtils.isNotBlank(jwt)) {
            userId = jwtService.getUserId(jwt);
        }
        return RestResult.success(auctionQueryService.queryAuctionDetails(userId, productId));
    }


    /**
     * @Author gaoqiang
     * @Description 定时任务生成订单失败时，手动调用生成订单
     * @Date 14:53 2019/5/20
     **/
    @GetMapping(value = "/rebuild/{recordId}")
    public RestResult rebuildExceptionOrder(@PathVariable Long recordId) {
        auctionTaskService.rebuildExceptionOrder(recordId);
        return RestResult.success();
    }
}