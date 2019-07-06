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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/05/27
*/
@ApiModel(value="com.dtlonline.shop.model.groupbuy.ProductGroupbuy")
@Data
@TableName(value = "product_groupbuy")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuy extends BaseObject {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 业务ID
     */
    @TableField(value = "txnId")
    @ApiModelProperty(value="业务ID")
    private String txnId;

    /**
     * 商品ID
     */
    @TableField(value = "productId")
    @ApiModelProperty(value="商品ID")
    private Long productId;

    /**
     * 团购标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="团购标题")
    private String title;

    /**
     * 团购类型：1 - 协议，2 - 意向 
     */
    @TableField(value = "type")
    @ApiModelProperty(value="团购类型：1 - 协议，2 - 意向 ")
    private Byte type;

    /**
     * 团购年月份
     */
    @TableField(value = "yearMonth")
    @ApiModelProperty(value="团购年月份")
    private LocalDate yearMonth;

    /**
     * 结算方式： 1 - 款到发货 ，2 - 货到付款
     */
    @TableField(value = "balanceType")
    @ApiModelProperty(value="结算方式： 1 - 款到发货 ，2 - 货到付款")
    private Byte balanceType;

    /**
     * 单个品牌最小达标量
     */
    @TableField(value = "minDiscountQuantity")
    @ApiModelProperty(value="单个品牌最小达标量")
    private Integer minDiscountQuantity;

    /**
     * 市场参考价格（团购类型为意向时存在）
     */
    @TableField(value = "marketPrice")
    @ApiModelProperty(value="市场参考价格（团购类型为意向时存在）")
    private BigDecimal marketPrice;

    /**
     * 报名开始日期
     */
    @TableField(value = "buyBeginDate")
    @ApiModelProperty(value="报名开始日期")
    private LocalDateTime buyBeginDate;

    /**
     * 报名截止日期
     */
    @TableField(value = "buyEndDate")
    @ApiModelProperty(value="报名截止日期")
    private LocalDateTime buyEndDate;

    /**
     * 活动截止日期
     */
    @TableField(value = "activityEndDate")
    @ApiModelProperty(value="活动截止日期")
    private LocalDateTime activityEndDate;

    /**
     * 团购已参数总量
     */
    @TableField(value = "buyTotalQuantity")
    @ApiModelProperty(value="团购已参数总量")
    private Integer buyTotalQuantity;

    /**
     * 是否已团购公示： 1 - 已公示，2 - 未公示
     */
    @TableField(value = "isPublished")
    @ApiModelProperty(value="是否已团购公示： 1 - 已公示，2 - 未公示")
    private Byte isPublished;

    /**
     * 是否置顶：1 - 是，2 - 否
     */
    @TableField(value = "isTop")
    @ApiModelProperty(value="是否置顶：1 - 是，2 - 否")
    private Byte isTop;

    /**
     * 状态： -1 - 已删除，1 - 待审核，2 - 审核通过（已上架），3 - 审核未通过，4 - 已下架
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态： -1 - 已删除，1 - 待审核，2 - 审核通过（已上架），3 - 审核未通过，4 - 已下架")
    private Byte status;

    /**
     * 创建人
     */
    @TableField(value = "userId")
    @ApiModelProperty(value="创建人")
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

    public static final String COL_TXNID = "txnId";

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_TITLE = "title";

    public static final String COL_TYPE = "type";

    public static final String COL_YEARMONTH = "yearMonth";

    public static final String COL_BALANCETYPE = "balanceType";

    public static final String COL_MINDISCOUNTQUANTITY = "minDiscountQuantity";

    public static final String COL_MARKETPRICE = "marketPrice";

    public static final String COL_BUYBEGINDATE = "buyBeginDate";

    public static final String COL_BUYENDDATE = "buyEndDate";

    public static final String COL_ACTIVITYENDDATE = "activityEndDate";

    public static final String COL_BUYTOTALQUANTITY = "buyTotalQuantity";

    public static final String COL_ISPUBLISHED = "isPublished";

    public static final String COL_ISTOP = "isTop";

    public static final String COL_STATUS = "status";

    public static final String COL_OPINION = "opinion";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}