package com.dtlonline.api.shop.command.store;

import com.dtlonline.api.isp.command.ImageDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.view.ProductCarInfoDTO;
import io.alpha.app.core.base.BasePageObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsRequireDTO")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StoreGoodsRequireDTOI extends BasePageObject {

    private String productInfo;
    private String productTitle;
    private Integer quantity;
    private Long id;
    private Long productId;
    private String title;
    private Integer type;
    private String requireProductInfo;

    @ApiModelProperty(value="商品图片")
    private List<ImageDTO> outProductImages;

    @ApiModelProperty(value="换入商品规格信息，支持多个。")
    List<StoreGoodsOutStandardInfoDTO> inStandardList;

    @ApiModelProperty(value="换出类别Id")
    private Long outCategoryId;
    @ApiModelProperty(value="换出商品规格信息，传仓单ID可以忽略此项")
    List<ProductStandardRecordDTO> outStandardList;

    @ApiModelProperty(value="借出数量")
    private Integer quantityLend;
    @ApiModelProperty(value="借入数量")
    private Integer quantityBorrow;
    @ApiModelProperty(value="交换数量")
    private Integer quantityExchange;
    @ApiModelProperty(value="卖出数量")
    private Integer quantitySell;
    @ApiModelProperty(value="买入数量")
    private Integer quantityBuy;

    @ApiModelProperty(value="共享价格")
    private BigDecimal unitPrice;

    @ApiModelProperty(value="txnId")
    private String txnId;

    @ApiModelProperty(value="发布人Id")
    private Long userId;

    @ApiModelProperty(value="仓单Id")
    private Long storeGoodsId;

    @ApiModelProperty(value="需求类型（共享类型）1借出  2借入 3交换  4卖出 5买入")
    private Integer tradeType;

    @ApiModelProperty(value="交易单下架日期")
    private LocalDate offSaleDate;

    @ApiModelProperty(value="每天回报率，每借出去一天就回报多少金额")
    private BigDecimal returnRate;

    @ApiModelProperty(value="商品保险金额")
    private BigDecimal insuranceMoney;

    @ApiModelProperty(value="商品保险开始日期")
    private LocalDate insuranceBeginDate;

    @ApiModelProperty(value="商品保险结束日期")
    private LocalDate insuranceEndDate;

    @ApiModelProperty(value="需求商品地区（借入方大概提货区域），只做展示")
    private String requireTradeZone;

    @ApiModelProperty(value="平台审核人")
    private String auditUser;

    @ApiModelProperty(value="审核意见")
    private String auditOpinion;

    @ApiModelProperty(value="状态 1通过 2不通过 3待审核")
    private Integer auditStatus;

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value="期货需求扩展信息")
    private StoreGoodsRequireFutureDTOI StoreGoodsRequireFuture;

    @ApiModelProperty(value="车辆需求扩展信息")
    private ProductCarInfoDTO productCarInfo;

    @ApiModelProperty(value="商品图片")
    private String productImgs;

    @ApiModelProperty(value = "仓库属性 1厂库 2中转库 3口岸仓库 4保税仓")
    private Integer storeAttr;

}