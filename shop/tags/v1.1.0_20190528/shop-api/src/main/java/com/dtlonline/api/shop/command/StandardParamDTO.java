package com.dtlonline.api.shop.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StandardParamDTO {

    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;
    /**
     * 标题
     */
    @NotBlank(message = "规格名称不能为空")
    private String title;
    /**
     * 输入方式 1 -文本框 2 -多选项 3 -单选项
     */
    @NotNull(message = "输入方式不能为空")
    private Integer type;
    /**
     * 规格数据
     */
    @NotBlank(message = "规格数据不能为空")
    private String data;

    @NotNull(message = "状态不能为空")
    private Integer status;
    /**
     * 是否必选 1 -必选 2 -非必选
     */
    @NotNull(message = "是否必选字段不能为空")
    private Integer required;

    private Long standardId;
}
