package com.dtlonline.api.shop.command;

import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttentionPackDTO extends BaseObject {

    /**
     * 业务唯一标识
     */
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;
    /**
     * 关注ID
     */
    @NotNull(message = "关注ID不能为空")
    private Long targetId;
    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private Integer type;
}
