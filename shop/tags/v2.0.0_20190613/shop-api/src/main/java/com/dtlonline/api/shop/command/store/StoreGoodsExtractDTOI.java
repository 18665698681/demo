package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BasePageObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="提货申请参数对象")
public class StoreGoodsExtractDTOI {

    @ApiModelProperty(value="txnId")
    @NotBlank(message = "txnId不能为空")
    private String txnId;

    @ApiModelProperty(value="提取数量")
    @NotNull(message = "提取数量不能为空")
    private Integer quantity;

    @ApiModelProperty(value="仓单ID")
    @NotNull(message = "请选择正确的仓单")
    private Long storeGoodsId;

    @ApiModelProperty(value="收货地址Id")
    @NotNull(message = "请选择收货地址")
    private Long addressId;

    @ApiModelProperty(value="用户Id")
    private Long userId;

    @ApiModelProperty(value="证件类型(1身份证)")
    @NotNull(message = "请选择身份证类型")
    private Integer papersType;

    @ApiModelProperty(value="证件号码")
    @NotBlank(message = "请填写证件号码")
    private String papersNumber;

    @ApiModelProperty(value="配送方式：1平台配送2自提")
    @JsonProperty("logisticsType")
    private Integer expressType;
}