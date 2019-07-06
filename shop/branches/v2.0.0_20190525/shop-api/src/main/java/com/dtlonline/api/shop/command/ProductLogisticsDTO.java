package com.dtlonline.api.shop.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel("发布物流商品参数对象")
public class ProductLogisticsDTO {

    @ApiModelProperty("运输类型 1-陆运 2-海运 3-空运")
    private Integer logisticsType;

    @ApiModelProperty("运输路线 1-专线物流 2-非装线物流")
    private Integer route;

    @ApiModelProperty("运输终点")
    private String target;

    @ApiModelProperty("运输起点")
    private String source;
}
