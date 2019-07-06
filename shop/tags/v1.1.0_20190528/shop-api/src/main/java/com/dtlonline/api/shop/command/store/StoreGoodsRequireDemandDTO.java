package com.dtlonline.api.shop.command.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="需求信息发布对象参数")
public class StoreGoodsRequireDemandDTO {

    /**
     * txnId
     */
    @ApiModelProperty(value="txnId")
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;

    /**
     * 发布商品信息 {"categoryId":123,"standradIds":[12,13,14]  }
     */
    @ApiModelProperty(value="发布商品信息 {'categoryId':123,'standradIds':[12,13,14]  }")
    @NotBlank(message = "商品信息不能为空")
    private String productInfo;

    /**
     * 标题
     */
    @ApiModelProperty(value="标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 需求类型（共享类型） 1借出 2借入 3交换  4卖出 5买入
     */
    @Min(value = 1,message = "需求类型有误")
    @NotNull(message = "需求类型不能为空")
    @ApiModelProperty(value="需求类型（共享类型） 1借出 2借入(可借) 3交换  4卖出 5买入(可卖)")
    private Integer tradeType;

    /**
     * 借出、借入、换入、换出的数量
     */
    @Min(value = 1,message = "数量输入错误")
    @ApiModelProperty(value="借出、借入、换入、换出的数量")
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    /**
     * 有效日期
     */
    @ApiModelProperty(value="有效日期")
    @NotNull(message = "有效日期不能为空")
    private LocalDate offSaleDate;

    /**
     * 每天回报率，每借出去一天就回报多少金额
     */
    @ApiModelProperty(value="每天回报率，每借出去一天就回报多少金额")
    private BigDecimal returnRate;

    /**
     * 需求商品地区（借入方大概提货区域），只做展示
     */
    @ApiModelProperty(value="需求商品地区（借入方大概提货区域），只做展示")
    @NotBlank(message = "需求商品地区不能为空")
    private String requireTradeZone;

}