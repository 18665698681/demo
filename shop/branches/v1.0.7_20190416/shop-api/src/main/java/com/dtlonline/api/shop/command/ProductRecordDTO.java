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
    /**
     * 业务唯一标识
     */
    @NotBlank(message = "商品唯一标识不能为空")
    @ApiModelProperty("业务唯一标识")
    private String txnId;
    /**
     * 商品标题
     */
    @NotBlank(message = "商品标题为必填")
    @Size(max = 200,message = "商品标题不能超过200字")
    @ApiModelProperty("商品标题")
    private String title;
    /**
     * 1-现货 2-物流 3-仓储 4-竞拍 99-采购
     */
    @NotNull(message = "发布类型不能为空")
    @ApiModelProperty("商品类型")
    private Integer type;
    /**
     * 品类ID
     */
    @ApiModelProperty("品类ID")
    private Long categoryId;
    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String categoryName;
    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long shopId;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private BigDecimal unitPrice;
    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stock;
    /**
     * 最小交易量
     */
    @ApiModelProperty("最小交易量")
    private Integer minDeal;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @JsonProperty("descriptions")
    private String description;
    /**
     * 是否显示价格 1-显示 2-不显示
     */
    @ApiModelProperty("是否显示价格 1-显示 2-不显示")
    private Integer showPrice;
    /**
     * 生产年份
     */
    @ApiModelProperty("生产年份")
    private String productionYear;
    /**
     * 是否开发票 1-开票 2-不开票
     */
    @ApiModelProperty("是否开发票 1-开票 2-不开票")
    private Integer invoice;
    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty("区")
    private String area;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;
    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer status;
    /**
     * 图片ID
     */
    private String imgs;
    /**
     * 有效时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expireTime;
    /**
     * 图片
     */
    private List imageList = new ArrayList();
    /**
     * 规格集合参数
     */
    List<ProductStandardRecordDTO> standardList = new ArrayList<>();
    /**
     * 竞拍信息
     */
    private AuctionApply auctionApply = new AuctionApply();
}
