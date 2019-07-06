package com.dtlonline.shop.model.groupbuy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyRecordDetailDTO;
import io.alpha.app.core.base.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
* Created by Mybatis Generator 2019/05/27
*/
@ApiModel(value="com.dtlonline.shop.model.groupbuy.ProductGroupbuyRecordDetail")
@Data
@TableName(value = "product_groupbuy_record_detail")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductGroupbuyRecordDetail extends BaseObject {
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
     * 团购报名ID
     */
    @TableField(value = "productGroupbuyRecordId")
    @ApiModelProperty(value="团购报名ID")
    private Long productGroupbuyRecordId;

    /**
     * 团购活动ID
     */
    @TableField(value = "productGroupbuyId")
    @ApiModelProperty(value="团购活动ID")
    private Long productGroupbuyId;

    /**
     * 商品ID
     */
    @TableField(value = "productId")
    @ApiModelProperty(value="商品ID")
    private Long productId;

    /**
     * 用户收货地址ID
     */
    @TableField(value = "userShippingAddressId")
    @ApiModelProperty(value="用户收货地址ID")
    private Long userShippingAddressId;

    /**
     * 品类ID
     */
    @TableField(value = "categoryId")
    @ApiModelProperty(value="品类ID")
    private Long categoryId;

    /**
     * 商品规格信息：{ "品类":"白砂糖","品级":"一级","品牌":"西江","规格":"50G/袋" }
     */
    @TableField(value = "standardInfo")
    @ApiModelProperty(value="商品规格信息：{ '品类':'白砂糖','品级':'一级','品牌':'西江','规格':'50G/袋' }")
    private String standardInfo;

    /**
     * 规格标题（白砂糖 大明山 一级）
     */
    @TableField(value = "standardTitle")
    @ApiModelProperty(value="规格标题（白砂糖 大明山 一级）")
    private String standardTitle;

    /**
     * 品牌名称（公示时使用）
     */
    @TableField(value = "standardProductTitle")
    @ApiModelProperty(value="品牌名称（公示时使用）")
    private String standardProductTitle;

    /**
     * 单批次最小配送量（吨）
     */
    @TableField(value = "minDeliveryQuantity")
    @ApiModelProperty(value="单批次最小配送量（吨）")
    private Integer minDeliveryQuantity;

    /**
     * 价格（元/吨）
     */
    @TableField(value = "price")
    @ApiModelProperty(value="价格（元/吨）")
    private BigDecimal price;

    /**
     * 团购数量（吨）
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="团购数量（吨）")
    private Integer quantity;

    /**
     * 商品总金额
     */
    @TableField(value = "totalAmount")
    @ApiModelProperty(value="商品总金额")
    private Long totalAmount;

    /**
     * 实际团购价格（单价）
     */
    @TableField(value = "actualPrice")
    @ApiModelProperty(value="实际团购价格（单价）")
    private BigDecimal actualPrice;

    /**
     * 实际商品总金额
     */
    @TableField(value = "actualTotalAmount")
    @ApiModelProperty(value="实际商品总金额")
    private BigDecimal actualTotalAmount;

    /**
     * 到货信息：[{"deliveryDate":"2019-05-11","deliveryQuantity":100},{"deliveryDate":"2019-05-20","DeliveryQuantity":200}]
     */
    @TableField(value = "deliveryData")
    @ApiModelProperty(value="到货信息：[{'deliveryDate':'2019-05-11','deliveryQuantity':100},{'deliveryDate':'2019-05-20','DeliveryQuantity':200}]")
    private String deliveryData;

    /**
     * 是否达标：1 - 已达到，2 - 未达到，3 - 未公示
     */
    @TableField(value = "isReachTarget")
    @ApiModelProperty(value="是否达标：1 - 已达到，2 - 未达到，3 - 未公示")
    private Byte isReachTarget;

    /**
     * 状态：1-有效 2-无效
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：1-有效 2-无效")
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
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="修改时间")
    private LocalDateTime lastModifyTime;

    public final static ProductGroupbuyRecordDetailDTO of(ProductGroupbuyRecordDetail groupbuyRecordDetail){
        ProductGroupbuyRecordDetailDTO productGroupbuyRecordDetailDTO = ProductGroupbuyRecordDetailDTO.builder().build();
        Optional.ofNullable(groupbuyRecordDetail).ifPresent(detail ->{
            BeanUtils.copyProperties(groupbuyRecordDetail,detail);
        });
        return productGroupbuyRecordDetailDTO;
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_TXNID = "txnId";

    public static final String COL_PRODUCTGROUPBUYRECORDID = "productGroupbuyRecordId";

    public static final String COL_PRODUCTGROUPBUYID = "productGroupbuyId";

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_USERSHIPPINGADDRESSID = "userShippingAddressId";

    public static final String COL_PRODUCTIVEYEAR = "productiveYear";

    public static final String COL_CATEGORYID = "categoryId";

    public static final String COL_STANDARDINFO = "standardInfo";

    public static final String COL_STANDARDTITLE = "standardTitle";

    public static final String COL_STANDARDPRODUCTTITLE = "standardProductTitle";

    public static final String COL_MINDELIVERYQUANTITY = "minDeliveryQuantity";

    public static final String COL_PRICE = "price";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_TOTALAMOUNT = "totalAmount";

    public static final String COL_ACTUALPRICE = "actualPrice";

    public static final String COL_ACTUALTOTALAMOUNT = "actualTotalAmount";

    public static final String COL_DELIVERYDATA = "deliveryData";

    public static final String COL_ISREACHTARGET = "isReachTarget";

    public static final String COL_STATUS = "status";

    public static final String COL_USERID = "userId";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}