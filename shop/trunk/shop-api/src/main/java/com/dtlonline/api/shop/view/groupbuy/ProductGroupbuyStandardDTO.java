package com.dtlonline.api.shop.view.groupbuy;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Auther: gaoqiang
 * @Date: 2019/5/27 14:41
 * @Description:
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="团购规则",description="团购规则")
public class ProductGroupbuyStandardDTO {

    @ApiModelProperty(value="商品ID")
    private Long productId;

    @ApiModelProperty(value="团购活动表ID")
    private Long productGroupbuyId;

    @ApiModelProperty(value="达标数量（吨）")
    private Integer targetQuantity;

    @ApiModelProperty(value="是否达标： 1 - 已达标，2 - 未达标，3 - 未公示")
    private Byte isReachTarget;

    @ApiModelProperty(value="达到目标数量的时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime reachTargetDate;

    @ApiModelProperty(value="团购价优惠比例（%）")
    private BigDecimal priceScale;

}
