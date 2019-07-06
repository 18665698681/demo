package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.store.model.store.StoreGoodsAuthRecord")
@Data
@TableName(value = "store_goods_auth_record")
public class StoreGoodsAuthRecord {
    /**
     * id
     */
     @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * txnId
     */
    @TableField(value = "txnId")
    @ApiModelProperty(value="txnId")
    private String txnId;

    /**
     * 商品Id  from  product_record 
     */
    @TableField(value = "productId")
    @ApiModelProperty(value="商品Id  from  product_record ")
    private Long productId;


    @TableField(value = "productInfo")
    @ApiModelProperty(value="productInfo")
    private String productInfo;


    @TableField(value = "productTitle")
    @ApiModelProperty(value="productTitle")
    private String productTitle;

    /**
     * 仓库
     */
    @TableField(value = "storeId")
    @ApiModelProperty(value="仓库")
    private Long storeId;

    /**
     * 每天仓储费，每存储一天就计算一天价格。
     */
    @TableField(value = "storageFee")
    @ApiModelProperty(value="每天仓储费，每存储一天就计算一天价格。")
    private BigDecimal storageFee;

    /**
     * 预计入库日期
     */
    @TableField(value = "inStoreDate")
    @ApiModelProperty(value="预计入库日期")
    private LocalDate inStoreDate;

    /**
     * 数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="数量")
    private Integer quantity;

    /**
     * 车辆车牌
     */
    @TableField(value = "carNumber")
    @ApiModelProperty(value="车辆车牌")
    private String carNumber;

    /**
     * 损耗标准，简单显示
     */
    @TableField(value = "lossSale")
    @ApiModelProperty(value="损耗标准，简单显示")
    private BigDecimal lossSale;

    /**
     * 保兑人签章
     */
    @TableField(value = "confirmer")
    @ApiModelProperty(value="保兑人签章")
    private String confirmer;

    /**
     * 保管人签章
     */
    @TableField(value = "preserver")
    @ApiModelProperty(value="保管人签章")
    private String preserver;

    /**
     * 关联合同
     */
    @TableField(value = "contract")
    @ApiModelProperty(value="关联合同")
    private String contract;

    /**
     * 商品保险金额
     */
    @TableField(value = "insuranceMoney")
    @ApiModelProperty(value="商品保险金额")
    private BigDecimal insuranceMoney;

    /**
     * 商品保险开始日期
     */
    @TableField(value = "insuranceBeginDate")
    @ApiModelProperty(value="商品保险开始日期")
    private LocalDate insuranceBeginDate;

    /**
     * 商品保险结束日期
     */
    @TableField(value = "insuranceEndDate")
    @ApiModelProperty(value="商品保险结束日期")
    private LocalDate insuranceEndDate;

    /**
     * 仓单持有人Id
     */
    @TableField(value = "userId")
    @ApiModelProperty(value="仓单持有人Id")
    private Long userId;

    /**
     * 审核人
     */
    @TableField(value = "auditUser")
    @ApiModelProperty(value="审核人")
    private String auditUser;


    /**
     * 状态(1通过2不通过3待审核)
     */
    @TableField(value = "auditStatus")
    @ApiModelProperty(value="状态(1通过2不通过3待审核)")
    private Integer auditStatus;

    /**
     * 状态(1正常2删除)
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态(1通过2不通过3待审核)")
    private Integer status;

    /**
     * 审核意见
     */
    @TableField(value = "auditOpinion")
    @ApiModelProperty(value="审核意见")
    private String auditOpinion;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_STOREID = "storeId";

    public static final String COL_STORAGEFEE = "storageFee";

    public static final String COL_INSTOREDATE = "inStoreDate";

    public static final String COL_CARNUMBER = "carNumber";

    public static final String COL_LOSSSALE = "lossSale";

    public static final String COL_CONFIRMER = "confirmer";

    public static final String COL_PRESERVER = "preserver";

    public static final String COL_CONTRACT = "contract";

    public static final String COL_insuranceMoney = "insuranceMoney";

    public static final String COL_INSURANCEBEGINDATE = "insuranceBeginDate";

    public static final String COL_INSURANCEENDDATE = "insuranceEndDate";

    public static final String COL_USERID = "userId";

    public static final String COL_AUDITUSER = "auditUser";

    public static final String COL_AUDITSTATUS = "auditStatus";

    public static final String COL_AUDITOPINION = "auditOpinion";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}