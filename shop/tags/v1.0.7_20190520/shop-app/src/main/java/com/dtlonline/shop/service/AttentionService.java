package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dtlonline.api.shop.command.AttentionPackDTO;
import com.dtlonline.shop.mapper.AttentionDao;
import com.dtlonline.shop.model.Attention;
import com.dtlonline.shop.view.AttentionDTO;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttentionService extends BaseService {

    @Autowired
    private AttentionDao attentionDao;

    public Map<Long,AttentionDTO> queryAttentionMap(Long userId){
        QueryWrapper<Attention> wrapper = new QueryWrapper();
        wrapper.eq("userId",userId);
        wrapper.eq("status",SUCCESS);
        List<Attention> list = attentionDao.selectList(wrapper);
        return list.stream().collect(Collectors.toMap(Attention::getTargetId, AttentionDTO::of));
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer saveAttention(List<AttentionPackDTO> attentionList,Long userId){
        attentionDao.updateAttention(userId);
        Integer result = 0;
        for (int i=0;i< attentionList.size();i++){
            Attention attention = new Attention();
            BeanUtils.copyProperties(attentionList.get(i),attention);
            attention.setUserId(userId);
            attention.setStatus(SUCCESS.intValue());
            result = attentionDao.insert(attention);
        }
        return result;
    }

    public Set<Long> queryAttentionForUserId(Long userId){
        QueryWrapper<Attention> wrapper = new QueryWrapper();
        wrapper.eq("status",SUCCESS);
        wrapper.eq("userId",userId);
        return attentionDao.selectList(wrapper).stream().map(Attention::getTargetId).collect(Collectors.toSet());
    }
}
