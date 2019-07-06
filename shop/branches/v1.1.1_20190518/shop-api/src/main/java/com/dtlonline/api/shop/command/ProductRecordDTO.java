package com.dtlonline.api.shop.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品信息参数对象")
public class ProductRecordDTO extends BaseObject {

    @NotBlank(message = "商品唯一标识不能为空")
    @ApiModelProperty("业务唯一标识")
    private String txnId;

    @NotBlank(message = "商品标题为必填")
    @Size(max = 200,message = "商品标题不能超过200字")
    @ApiModelProperty("商品标题")
    private String title;

    @NotNull(message = "发布类型不能为空")
    @ApiModelProperty("商品类型")
    private Integer type;

    @ApiModelProperty("品类ID")
    private Long categoryId;

    @ApiModelProperty("品类名称")
    private String categoryName;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("最小交易量")
    private Integer minDeal;

    @ApiModelProperty("备注")
    @JsonProperty("descriptions")
    private String description;

    @ApiModelProperty("是否显示价格 1-显示 2-不显示")
    private Integer showPrice;

    @ApiModelProperty("生产年份")
    private String productionYear;

    @ApiModelProperty("是否开发票 1-开票 2-不开票")
    private Integer invoice;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("审核状态")
    private Integer status;

    @ApiModelProperty("商品图片ID")
    private String imgs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("有效日期")
    private LocalDate expireTime;

    @ApiModelProperty("图片对象")
    private List imageList = new ArrayList();

    @ApiModelProperty("规格集合参数")
    List<ProductStandardRecordDTO> standardList = new ArrayList<>();

    @ApiModelProperty("竞拍信息参数")
    private AuctionApply auctionApply = new AuctionApply();

    @ApiModelProperty("物流参数对象")
    private ProductLogisticsDTO logistics;
}
