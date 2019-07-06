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
@ApiModel(value="com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecord")
@Data
@TableName(value = "product_groupbuy_record")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuyRecord extends BaseObject {
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
     * 团购活动ID
     */
    @TableField(value = "productGroupbuyId")
    @ApiModelProperty(value="团购活动ID")
    private Long productGroupbuyId;

    /**
     * 生产年份
     */
    @ApiModelProperty("生产年份")
    private Integer productiveYear;

    /**
     * 结算方式： 1 - 款到发货 ，2 - 货到付款
     */
    @TableField(value = "balanceType")
    @ApiModelProperty(value="结算方式： 1 - 款到发货 ，2 - 货到付款")
    private Byte balanceType;

    /**
     * 参与量（吨）
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="参与量（吨）")
    private Integer quantity;

    /**
     * 交易总金额
     */
    @TableField(value = "totalAmount")
    @ApiModelProperty(value="交易总金额")
    private BigDecimal totalAmount;

    /**
     * 实际交易总金额
     */
    @TableField(value = "actualTotalAmount")
    @ApiModelProperty(value="实际交易总金额")
    private BigDecimal actualTotalAmount;

    /**
     * 买家团购合同URL ID，多个用逗号分隔
     */
    @TableField(value = "buyerContractIds")
    @ApiModelProperty(value="买家团购合同URL ID，多个用逗号分隔")
    private String buyerContractIds;

    /**
     * 卖家团购合同URL ID，多个用逗号分隔
     */
    @TableField(value = "sellerContractIds")
    @ApiModelProperty(value="卖家团购合同URL ID，多个用逗号分隔")
    private String sellerContractIds;

    /**
     * 补充合同路径ID，多个用逗号分隔
     */
    @TableField(value = "supplementaryContractIds")
    @ApiModelProperty(value="补充合同路径ID，多个用逗号分隔")
    private String supplementaryContractIds;

    /**
     * 支付方式：1 - 线上，2 - 线下
     */
    @TableField(value = "paymentMethod")
    @ApiModelProperty(value="支付方式：1 - 线上，2 - 线下")
    private Byte paymentMethod;

    /**
     * 状态：-1 - 已删除 1 - 已通过 2 - 不通过 3 - 待审核 4 - 待撮合
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：-1 - 已删除 1 - 已通过 2 - 不通过 3 - 待审核 4 - 待撮合")
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

    public static final String COL_TXNID = "txnId";

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_PRODUCTGROUPBUYID = "productGroupbuyId";

    public static final String COL_BALANCETYPE = "balanceType";

    public static final String COL_PRICE = "price";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_TOTALAMOUNT = "totalAmount";

    public static final String COL_ACTUALTOTALAMOUNT = "actualTotalAmount";

    public static final String COL_BUYERCONTRACTIDS = "buyerContractIds";

    public static final String COL_SELLERCONTRACTIDS = "sellerContractIds";

    public static final String COL_SUPPLEMENTARYCONTRACTIDS = "supplementaryContractIds";

    public static final String COL_PAYMENTMETHOD = "paymentMethod";

    public static final String COL_STATUS = "status";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}