package com.dtlonline.api.shop.command;

import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FinanceDTO extends BaseObject {
    private String txnId;
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空")
    private String name;
    /**
     * 联系人
     */
    @NotBlank(message = "联系人名称不能为空")
    private String contact;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号码不能为空")
    private String mobile;
    /**
     * 1-金融 2-借换
     */
    private Integer type;
    /**
     * 其他需求
     */
    private String other;
}
