package com.dtlonline.api.shop.command.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("新增团活动规则对象参数")
public class ProductGroupbuyStandardRequestDTO {

    /**
     * 达标数量（吨）
     */
    @ApiModelProperty(value = "达标数量")
    @NotNull(message = "请输入达标数量")
    @Min(value = 1, message = "达标数量不能小于1")
    private Double targetWeight;

    /**
     * 团购价优惠比例
     */
    @ApiModelProperty(value = "团购价优惠比例")
    @NotNull(message = "请输入团购价优惠比例")
    @DecimalMin(value = "0.01", message = "团购价优惠比例不能小于0.01")
    private BigDecimal priceScale;

}
