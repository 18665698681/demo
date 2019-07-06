package com.dtlonline.api.shop.view.store;

import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsExtract")
@Data

public class StoreGoodsExtractDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value="txnId")
    private String txnId;

    @ApiModelProperty(value="提取数量")
    private Integer quantity;

    @ApiModelProperty(value="仓单ID")
    private Long storeGoodsId;

    @ApiModelProperty(value="商品规格信息")
    private String  productTitle;

    @ApiModelProperty(value="商品规格-类别")
    private String categoryName;

    @ApiModelProperty(value="商品标题-规格")
    private List<ProductStandardRecordDTO> productStandardRecordDTOs;

    @ApiModelProperty(value="仓单编号")
    private String goodsNo;

    @ApiModelProperty(value="存货凭证编号")
    private String inventoryNo;

    @ApiModelProperty(value="收货地址Id")
    private Long addressId;

    @ApiModelProperty(value="用户Id")
    private Long userId;

    @ApiModelProperty(value="运费")
    private BigDecimal logisticsCosts;

    @ApiModelProperty(value="证件类型(1身份证)")
    private Integer papersType;

    @ApiModelProperty(value="证件号码")
    private String papersNumber;

    @ApiModelProperty(value="状态 1通过 2不通过 3待审核 4已出库")
    private Integer auditStatus;

    @ApiModelProperty(value="收货地址")
    private String address;

    @ApiModelProperty(value="配送方式：1平台配送2自提")
    private Integer expressType;

    @ApiModelProperty(value="仓库信息")
    private StoreGoodsDTO storeGoodsDTO;

}