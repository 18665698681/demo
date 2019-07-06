package com.dtlonline.api.shop.view.groupbuy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@ApiModel(value="团购品牌",description="团购品牌")
public class ProductGroupbuyBrandDTO {

    @ApiModelProperty(value="品牌名称")
    private String brandName;

    @ApiModelProperty(value="数量 ")
    private Integer quantity;

}
