package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtlonline.api.shop.view.groupbuy.AuditRecordResponseDTO;
import com.dtlonline.api.user.remote.UserInfosRemoteService;
import com.dtlonline.api.user.view.UserDTO;
import com.dtlonline.shop.mapper.groupbuy.AuditRecordDao;
import com.dtlonline.shop.model.groupbuy.AuditRecord;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.ObjectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 审核记录service
 * @author ken
 * @date 2019-05-20 14:35:23
 */
@Service
public class AuditRecordService extends BaseService {

    @Autowired
    private AuditRecordDao auditRecordDao;

    @Autowired
    private UserInfosRemoteService userInfosRemoteService;

    /**
     * 根据审核业务数据ID获取对应的审核记录列表
     * @param auditDataId 审核数据ID，即业务ID
     * @return List<AuditRecordResponseDTO> 团购规则列表
     */
    public List<AuditRecordResponseDTO> queryListByAuditDataId(Long auditDataId) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", 1);
        wrapper.eq("auditDataId", auditDataId);

        List<AuditRecord> auditRecords = auditRecordDao.selectList(wrapper);
        if (CollectionUtils.isEmpty(auditRecords)) {
            return Collections.EMPTY_LIST;
        }

        List<AuditRecordResponseDTO> auditRecordResponseDTOs = ObjectUtils.copy(auditRecords, AuditRecordResponseDTO.class);

        auditRecordResponseDTOs.forEach(a -> {

            Long userId = a.getUserId();
            UserDTO userDto = userInfosRemoteService.infos(null, userId).getData();
            if (userDto != null) {
                a.setAuditor(userDto.getNikeName());
            }
        });

        return auditRecordResponseDTOs;
    }

}
