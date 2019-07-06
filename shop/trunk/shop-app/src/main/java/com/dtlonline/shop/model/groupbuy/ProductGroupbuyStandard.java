package com.dtlonline.shop.model.groupbuy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/05/27
*/
@ApiModel(value="com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard")
@Data
@TableName(value = "product_groupbuy_standard")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuyStandard extends BaseObject {
    /**
     * 主键
     */
     @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商品ID
     */
    @TableField(value = "productId")
    @ApiModelProperty(value="商品ID")
    private Long productId;

    /**
     * 团购活动表ID
     */
    @TableField(value = "productGroupbuyId")
    @ApiModelProperty(value="团购活动表ID")
    private Long productGroupbuyId;

    /**
     * 达标数量（吨）
     */
    @TableField(value = "targetQuantity")
    @ApiModelProperty(value="达标数量（吨）")
    private Integer targetQuantity;

    /**
     * 是否达标： 1 - 已达标，2 - 未达标，3 - 未公示
     */
    @TableField(value = "isReachTarget")
    @ApiModelProperty(value="是否达标： 1 - 已达标，2 - 未达标，3 - 未公示")
    private Byte isReachTarget;

    /**
     * 达到目标数量的时间
     */
    @TableField(value = "reachTargetDate")
    @ApiModelProperty(value="达到目标数量的时间")
    private LocalDateTime reachTargetDate;

    /**
     * 团购价优惠比例（%）
     */
    @TableField(value = "priceScale")
    @ApiModelProperty(value="团购价优惠比例（%）")
    private BigDecimal priceScale;

    /**
     * 状态：-1 - 已删除，1 - 有效
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：-1 - 已删除，1 - 有效")
    private Byte status;

    /**
     * 创建人ID
     */
    @TableField(value = "userId")
    @ApiModelProperty(value="创建人ID")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="修改时间")
    private LocalDateTime lastModifyTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_PRODUCTGROUPBUYID = "productGroupbuyId";

    public static final String COL_TARGETQUANTITY = "targetQuantity";

    public static final String COL_JOINQUANTITY = "joinQuantity";

    public static final String COL_ISREACHTARGET = "isReachTarget";

    public static final String COL_REACHTARGETJOINQUANTITY = "reachTargetJoinQuantity";

    public static final String COL_REACHTARGETDATE = "reachTargetDate";

    public static final String COL_PRICESCALE = "priceScale";

    public static final String COL_STATUS = "status";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}