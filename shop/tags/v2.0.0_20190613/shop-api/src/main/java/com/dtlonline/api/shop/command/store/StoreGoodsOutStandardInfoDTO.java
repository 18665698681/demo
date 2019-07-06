package com.dtlonline.api.shop.command.store;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("换入商品规格对象")
public class StoreGoodsOutStandardInfoDTO {

    @ApiModelProperty(value="类别")
    private Long inCategoryId;
    @ApiModelProperty(value="一组规格")
    private List<ProductStandardRecordDTO> inStandardList;
}
