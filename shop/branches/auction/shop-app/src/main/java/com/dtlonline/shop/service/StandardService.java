package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.shop.mapper.StandardDao;
import com.dtlonline.shop.model.Standard;
import com.dtlonline.shop.view.StandardDTO;
import io.alpha.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StandardService extends BaseService{

    @Autowired
    private StandardDao standardDao;

    public List<StandardDTO> queryStandardForCategoryId(Long categoryId){
        QueryWrapper<Standard> wrapper = new QueryWrapper<Standard>();
        wrapper.eq("categoryId",categoryId);
        wrapper.eq("status", SUCCESS);
        List<Standard> standardList = standardDao.selectList(wrapper);
        List<StandardDTO> standardDTOList = standardList.stream().map(StandardDTO::of).collect(Collectors.toList());
        return standardDTOList;
    }
}
