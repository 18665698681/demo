package com.dtlonline.api.shop.command.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 商品规格信息，用来合并多个仓单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StoreGoodsProductInfoDTO {

    @ApiModelProperty(value="类别")
    private Long categoryId;
    @ApiModelProperty(value="一组规格Id")
    private List<Long> productStandardRecordIds;
}
