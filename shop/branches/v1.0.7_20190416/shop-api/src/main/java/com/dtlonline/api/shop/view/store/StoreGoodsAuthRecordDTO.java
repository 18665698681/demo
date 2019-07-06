package com.dtlonline.api.shop.view.store;

import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="我的仓单申请列表")
public class StoreGoodsAuthRecordDTO {

    @ApiModelProperty("仓单申请ID")
    private Long id;

    @ApiModelProperty(value="txnId")
    private String txnId;

    @ApiModelProperty("仓单标题")
    private String title;

    @ApiModelProperty("仓单编号")
    private String goodsNo;

    @ApiModelProperty("类型 5-仓单")
    private Integer type;

    @ApiModelProperty("商品Id  from  product_record ")
    private Long productId;

    @ApiModelProperty("商品规格信息 ")
    private String productInfo;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("仓库")
    private String storeName;

    @ApiModelProperty("仓库地址")
    private String storeAddress;

    @ApiModelProperty("仓库ID")
    private Long storeId;

    @ApiModelProperty("发布状态")
    private Integer status;

    @ApiModelProperty("审核状态 1-已通过 2-审核拒绝 3-待审核")
    private Integer auditStatus;

    @ApiModelProperty("借出数量")
    private Integer lendQuantity;

    @ApiModelProperty("冻结数量")
    private Integer validQuantity;

    @ApiModelProperty("仓单图片")
    private String imgPath;

    @ApiModelProperty(value="商品规格标题")
    private String productTitle;

    @ApiModelProperty(value="商品标题-类型")
    private String productTitleCategory;
    @ApiModelProperty(value="商品标题-规格")
    private String productTitleStandards;

    @ApiModelProperty(value="商品规格-类别")
    private String categoryName;
    @ApiModelProperty(value="商品标题-规格")
    private List<ProductStandardRecordDTO> productStandardRecordDTOs;

    @ApiModelProperty(value="预计入库日期")
    private LocalDate inStoreDate;

    @ApiModelProperty(value="车辆车牌")
    private String carNumber;

    @ApiModelProperty(value="商品保险金额")
    private BigDecimal insuranceMoney;

    @ApiModelProperty(value="商品保险开始日期")
    private LocalDate insuranceBeginDate;

    @ApiModelProperty(value="商品保险结束日期")
    private LocalDate insuranceEndDate;

    @ApiModelProperty(value="商品信息")
    private ProductRecordDTO productDTO;
}