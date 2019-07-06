package com.dtlonline.shop.controller.personal;

import com.dtlonline.api.shop.command.FinanceDTO;
import com.dtlonline.shop.service.FinanceService;
import io.alpha.core.base.BaseController;
import io.alpha.core.util.JsonUtils;
import io.alpha.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/personal")
public class FinanceController extends BaseController {

    @Autowired
    private FinanceService financeService;

    /**
     * 金融/借换 信息保存
     */
    @PostMapping("/finances")
    public RestResult saveFinance(@RequestBody @Valid FinanceDTO financeDTO){
        logger.info("提交报价信息[{}]", JsonUtils.toJSON(financeDTO));
        financeService.saveFinance(financeDTO);
        return RestResult.success();
    }
}
