package com.dtlonline.api.shop.view.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="团购报名明细",description="团购报名明细")
public class ProductGroupbuyRecordDetailListDTO {

    @ApiModelProperty("团购标题")
    private String standardProductTitle;

    @ApiModelProperty("优惠比例")
    private BigDecimal priceScale;

    @ApiModelProperty("达标量")
    private Integer targetQuantity;

    @ApiModelProperty("是否达标 1-已达标，2-未达标")
    private Byte isReachTarget;

    @ApiModelProperty("达标日期")
    private String reachTargetDate;
}
