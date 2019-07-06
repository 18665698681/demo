package com.dtlonline.api.shop.view.store;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
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
public class StoreGoodsProductTitleDTO {

    @ApiModelProperty(value="商品规格-类别")
    private String categoryName;
    @ApiModelProperty(value="商品标题-规格")
    private List<ProductStandardRecordDTO> productStandardRecordDTOs;
}
