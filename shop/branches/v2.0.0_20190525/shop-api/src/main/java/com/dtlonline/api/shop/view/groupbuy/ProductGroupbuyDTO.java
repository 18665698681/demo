package com.dtlonline.api.shop.view.groupbuy;

import com.dtlonline.api.shop.view.CategoryDTOI;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Auther: gaoqiang
 * @Date: 2019/5/27 14:41
 * @Description:
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="团购",description="团购")
public class ProductGroupbuyDTO {

    @ApiModelProperty(value="主键")
    private Long id;

    @ApiModelProperty(value="业务ID")
    private String txnId;

    @ApiModelProperty(value="商品ID")
    private Long productId;

    @ApiModelProperty(value="团购标题")
    private String title;

    @ApiModelProperty(value="团购类型：1 - 协议，2 - 意向 ")
    private Byte type;

    @ApiModelProperty(value="团购年月份")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate yearMonth;

    @ApiModelProperty(value="结算方式： 1 - 款到发货 ，2 - 货到付款")
    private Byte balanceType;

    @ApiModelProperty(value="单个品牌最小达标量")
    private Integer minDiscountQuantity;

    @ApiModelProperty(value="市场参考价格（团购类型为意向时存在）")
    private BigDecimal marketPrice;

    @ApiModelProperty(value="报名开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate buyBeginDate;

    @ApiModelProperty(value="报名截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate buyEndDate;

    @ApiModelProperty(value="活动截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate activityEndDate;

    @ApiModelProperty(value="团购已参数总量")
    private Integer buyTotalQuantity;

    @ApiModelProperty(value="是否已团购公示： 1 - 已公示，2 - 未公示")
    private Byte isPublished;

    @ApiModelProperty(value="是否置顶：1 - 是，2 - 否")
    private Byte isTop;

    @ApiModelProperty(value="状态： -1 - 已删除，1 - 待审核，2 - 审核通过（已上架），3 - 审核未通过，4 - 已下架")
    private Byte status;

    @ApiModelProperty(value="审核意见")
    private String opinion;

    @ApiModelProperty(value="创建人")
    private Long userId;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value="最高优惠")
    private BigDecimal maxPriceScale;

    @ApiModelProperty(value="流程状态(1未开始2进行中3已结束)")
    private Integer processStatus;

    @ApiModelProperty(value = "参与人数")
    private Integer joinUserQuantity;

    @ApiModelProperty(value="商品信息")
    private ProductInfoDTO productInfoDTO;

    @ApiModelProperty(value="达标规则信息")
    private List<ProductGroupbuyStandardDTO> productGroupbuyStandardDTO;

    @ApiModelProperty(value="分类信息")
    private CategoryDTOI categoryDTOI;

    @ApiModelProperty(value="参与品牌信息")
    private List<ProductGroupbuyBrandDTO> brandDTOS;

}
