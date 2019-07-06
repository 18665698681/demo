package com.dtlonline.api.shop.view.groupbuy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditRecordResponseDTO extends BaseObject {

    /**
     * ID
     */
    private Long id;

    /**
     * 审核意见
     */
    private String opinion;

    /**
     * 审核结果： -1 - 未通过，1 - 已通过
     */
    private Integer result;

    /**
     * 审核结果中文描述
     */
    private String resultZh;

    /**
     * 报名截止时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("auditDate")
    private LocalDateTime createTime;

    /**
     * 审核人ID
     */
    private Long userId;

    /**
     * 审核人
     */
    private String auditor;

    public void setResult(Integer result) {

        this.result = result;
        if (result != null && result.intValue() == 1) {
            this.resultZh = "通过";
        } else {
            this.resultZh = "未通过";
        }
    }
}
