package com.dtlonline.api.shop.remote.activity;

import com.dtlonline.api.shop.view.activity.ActivityConfigDTO;
import io.alpha.app.core.view.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "activityConfigRemoteService")
public interface ActivityConfigRemoteService {

    @GetMapping("/internal/activity/config")
    RestResult<List<ActivityConfigDTO>> queryActivityConfigForList();
}
