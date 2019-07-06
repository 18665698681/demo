package com.dtlonline.shop.controller.internal.activity;

import com.dtlonline.api.shop.remote.activity.ActivityConfigRemoteService;
import com.dtlonline.api.shop.view.activity.ActivityConfigDTO;
import com.dtlonline.shop.service.activity.ActivityConfigService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/activity/")
public class ActivityConfigController extends BaseController implements ActivityConfigRemoteService {

    @Autowired
    private ActivityConfigService activityConfigService;

    @Override
    @GetMapping("/config")
    public RestResult<List<ActivityConfigDTO>> queryActivityConfigForList(){
        return RestResult.success(activityConfigService.queryActivityConfigForList());
    }
}
