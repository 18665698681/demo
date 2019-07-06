package com.dtlonline.shop.controller.internal.groupbuy;

import com.dtlonline.api.shop.remote.groupbuy.ProductGroupbuyRemoteService;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyDTO;
import com.dtlonline.shop.service.groupbuy.ProductGroupbuyService;
import io.alpha.app.core.view.RestResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: gaoqiang
 * @Date: 2019/5/27 13:51
 * @Description:团购活动
 */
@RestController("productGoupbuyController")
@RequestMapping("internal/products/groupbuy")
@Api(tags = {"团购活动"})
public class ProductGoupbuyController implements ProductGroupbuyRemoteService {

    @Autowired
    private ProductGroupbuyService productGroupbuyService;

    @GetMapping(value = "/list")
    @Override
    public RestResult<Page<ProductGroupbuyDTO>> queryList(@RequestParam(defaultValue = "1",value = "current") Integer current,
                                                          @RequestParam(defaultValue = "10",value = "size") Integer size,
                                                          @RequestParam(defaultValue = "2",value="process") Integer process)  {
        return RestResult.success(productGroupbuyService.queryListInPage(current,size,process));
    }

    @GetMapping(value = "/detail/{recordId}")
    @Override
    public RestResult<ProductGroupbuyDTO> queryDetails(@PathVariable("recordId") Long recordId) {
        return RestResult.success(productGroupbuyService.queryGroupbuyDetails(recordId));
    }
}
