package com.dtlonline.shop.controller.groupbuy;

import com.dtlonline.shop.service.groupbuy.AuditRecordService;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.view.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审核记录controller
 * @author ken
 * @date 2019-05-20 16:55:50
 */
@RestController("AuditRecordController")
@RequestMapping("/groupbuy")
public class AuditRecordController extends BaseController {

    @Autowired
    private AuditRecordService auditRecordService;

    /**
     * 查询审核记录列表
     */
    @GetMapping(value = "/audit/list/{auditDataId}")
    public RestResult queryAuditRecordList(@PathVariable Long auditDataId) {

        return RestResult.success(auditRecordService.queryListByAuditDataId(auditDataId));
    }

}
