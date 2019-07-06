package com.dtlonline.shop.service.activity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.view.activity.ActivityConfigDTO;
import com.dtlonline.shop.mapper.activity.ActivityConfigMapper;
import com.dtlonline.shop.model.activity.ActivityConfig;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConfigService extends BaseService {

    @Autowired
    private ActivityConfigMapper activityConfigMapper;

    public List<ActivityConfigDTO> queryActivityConfigForList(){
        QueryWrapper<ActivityConfig> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("`index`");
        List<ActivityConfig> activityConfigs = activityConfigMapper.selectList(wrapper);
        return activityConfigs.stream().map(ActivityConfig::of).collect(Collectors.toList());
    }
}