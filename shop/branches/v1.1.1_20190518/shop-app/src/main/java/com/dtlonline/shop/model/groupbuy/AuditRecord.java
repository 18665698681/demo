package com.dtlonline.shop.model.groupbuy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 审核记录entity
 * @author ken
 * @date 2019-05-20 14:20:50
 */
@Data
@TableName("audit_record")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuditRecord extends BaseObject {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 业务ID
     */
    private String txnId;

    /**
     * 审核数据ID
     */
    private Long auditDataId;

    /**
     * 审核数据业务ID
     */
    private String auditDataTxnId;

    /**
     * 审核意见
     */
    private String opinion;

    /**
     * 审核结果： -1 - 未通过，1 - 已通过
     */
    private Integer result;

    /**
     * 删除状态： -1 - 已删除 ，1 - 有效
     **/
    private Integer status;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifyTime;

}
