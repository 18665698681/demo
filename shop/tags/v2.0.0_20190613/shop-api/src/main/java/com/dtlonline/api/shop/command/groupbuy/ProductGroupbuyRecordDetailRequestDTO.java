package com.dtlonline.api.shop.command.groupbuy;


import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel("报名信息参数对象")
public class ProductGroupbuyRecordDetailRequestDTO {

    @ApiModelProperty("分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty("规格信息")
    @NotNull.List({@NotNull(groups = ProductStandardRecordDTO.class,message = "规格信息不能为空")})
    private List<ProductStandardRecordDTO> standardList;

    @ApiModelProperty("参与数量")
    @NotNull(message = "参与数量不能为空")
    @Min(value = 0,message = "参与数量必须大于0")
    private Integer quantity;

    @ApiModelProperty("单批最小配送量")
    @NotNull(message = "单批最小配送量不能为空")
    @Min(value = 0,message = "单批最小配送量必须大于0")
    private Integer minDeliveryQuantity;

    @ApiModelProperty("收货地址ID")
    @NotNull(message = "收货地址不能为空")
    @Min(value = 0,message = "收货地址有误,请重新选择")
    private Long addressId;

    @ApiModelProperty("团购分批发货 [{deliveryDate:2019-05-11,deliveryQuantity:100},{deliveryDate:2019-05-20,DeliveryQuantity:200}]")
    private String deliveryData;
}
