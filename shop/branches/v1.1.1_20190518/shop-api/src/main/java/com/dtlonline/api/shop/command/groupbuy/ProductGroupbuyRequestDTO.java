package com.dtlonline.api.shop.command.groupbuy;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("新增团活动对象参数")
public class ProductGroupbuyRequestDTO {

    /**
     * 团购活动ID（修改时必填）
     */
    @ApiModelProperty(value = "团购活动ID")
    private Long id;

    /**
     * 业务唯一标识
     */
    @ApiModelProperty(value = "业务ID")
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;

    /**
     * 团购类型
     */
    @ApiModelProperty(value = "团购类型：1-协议，2-意向")
    @NotNull(message = "请选择团购类型")
    @Min(value = 1, message = "团购类型不符合")
    @Max(value = 2, message = "团购类型不符合")
    private Integer type;

    /**
     * 团购标题
     */
    @ApiModelProperty(value = "团购标题")
    @NotBlank(message = "请输入团购标题")
    private String title;

    /**
     * 团购年份
     */
    @ApiModelProperty(value = "团购年份")
    @NotBlank(message = "请输入团购年份")
    private String year;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    @NotBlank(message = "请输入团购月份")
    private String month;

    /**
     * 品种ID（一级）
     */
    @ApiModelProperty(value = "货品类型")
    @NotNull(message = "请选择货品类型")
    private Long categoryId;

    /**
     * 品种名称
     */
    @ApiModelProperty(value = "品种名称")
    @NotBlank(message = "品种名称不能为空")
    private String categoryTitle;

    /**
     * 结算方式： 1 - 款到发货 ，2 - 货到付款
     */
    @ApiModelProperty(value = "结算方式：1-款到发货 ，2-货到付款")
    @NotNull(message = "请选择结算方式")
    @Min(value = 1, message = "结算方式不符合")
    @Max(value = 2, message = "结算方式不符合")
    private Integer balanceType;

    /**
     * 最小交易量（吨）
     */
    @ApiModelProperty(value = "最小起团量")
    @NotNull(message = "请输入最小起团量")
    @Min(value = 1, message = "最小交易量不能小于1")
    private Double minTrade;

    /**
     * 单个品牌最小达标量
     */
    @ApiModelProperty(value = "单品牌达标量")
    @NotNull(message = "请输入单品牌达标量")
    @Min(value = 1, message = "单品牌达标量不能小于1")
    private Double minDiscountWeight;

    /**
     * 市场参考价格
     */
    @ApiModelProperty(value = "市场参考价格")
    @DecimalMin(value = "0.01", message = "市场参考价格不能小于0.01")
    private BigDecimal marketPrice;

    /**
     * 报名截止日期
     */
    @ApiModelProperty(value = "报名截止日期")
    @NotNull(message = "请选择报名截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate buyEndDate;

    /**
     * 活动结束日期
     */
    @ApiModelProperty(value = "活动结束日期")
    @NotNull(message = "请选择活动结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate activityEndDate;

    /**
     * 展示图片
     */
    @ApiModelProperty(value = "展示图片")
    @NotBlank(message = "请上传展示图片")
    private String showImageIds;

    /**
     * 项目简介
     */
    @ApiModelProperty(value = "展示详情")
    @NotBlank(message = "请输入展示详情")
    private String description;

    /**
     * 活动规则列表
     */
    @ApiModelProperty(value = "活动规则列表")
    @NotNull.List({@NotNull(groups=ProductGroupbuyStandardRequestDTO.class,message="活动规则列表不能为空")})
    private List<ProductGroupbuyStandardRequestDTO> standardRequestDTOs = new ArrayList<>();

}
