package com.dtlonline.api.shop.view.store;


import com.dtlonline.api.shop.command.ProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoods")
@Data

public class StoreGoodsMergeQuantityDTO {



    @ApiModelProperty(value="存货凭证编号")
    private String inventoryNo;

    @ApiModelProperty(value="商品规格id")
    private String productInfo;

    @ApiModelProperty(value="商品规格标题")
    private String productTitle;

    @ApiModelProperty(value="仓单持有人Id")
    private Long userId;

    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value="统计：剩余可用数量")
    private Integer sumValidQuantity;

    @ApiModelProperty(value="统计：已借出数量")
    private Integer sumLendingQuantity;

    @ApiModelProperty(value="统计：冻结数量数量")
    private Integer sumFrozenQuantity;

    @ApiModelProperty(value="统计：全部资产数量")
    private Integer sumAllQuantity;

}