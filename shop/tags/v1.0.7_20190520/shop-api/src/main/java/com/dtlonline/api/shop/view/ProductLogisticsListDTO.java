package com.dtlonline.api.shop.view;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductLogisticsListDTO {

    @ApiModelProperty("业务唯一标识")
    private String txnId;

    @ApiModelProperty("运输类型 1-陆运 2-海运 3-空运")
    private Integer logisticsType;

    @ApiModelProperty("运输类型名称 陆运/海运/空运")
    private String logisticsTypeName;

    @ApiModelProperty("运输路线 1-专线物流 2-非装线物流")
    private Integer route;

    @ApiModelProperty("运输路线 专线物流/非装线物流")
    private String routeName;

    @ApiModelProperty("运输起点")
    private String target;

    @ApiModelProperty("运输终点")
    private String source;
}
