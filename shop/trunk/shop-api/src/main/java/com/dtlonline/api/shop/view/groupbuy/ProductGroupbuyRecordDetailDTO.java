package com.dtlonline.api.shop.view.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="团购报名明细",description="团购报名明细")
public class ProductGroupbuyRecordDetailDTO {

    @ApiModelProperty("团购标题")
    private String standardProductTitle;


    @ApiModelProperty("是否达标 1-已达标，2-未达标")
    private Byte isReachTarget;

}
