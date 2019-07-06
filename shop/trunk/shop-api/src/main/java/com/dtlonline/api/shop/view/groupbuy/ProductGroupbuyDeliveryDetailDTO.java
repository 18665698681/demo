package com.dtlonline.api.shop.view.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value="团购分批发货",description="团购分批发货")
public class ProductGroupbuyDeliveryDetailDTO {

    @ApiModelProperty("发货日期")
    private LocalDate deliveryDate;

    @ApiModelProperty("数量")
    private Integer deliveryQuantity;

}
