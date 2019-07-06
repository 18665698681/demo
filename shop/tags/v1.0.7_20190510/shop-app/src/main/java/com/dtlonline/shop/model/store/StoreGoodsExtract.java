package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.store.model.store.StoreGoodsExtract")
@Data
@TableName(value = "store_goods_extract")
public class StoreGoodsExtract {
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
     * 数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="数量")
    private Integer quantity;

    @TableField(value = "storeGoodsId")
    @ApiModelProperty(value="要提货的仓单")
    private Long storeGoodsId;

    @TableField(value = "splitStoreGoodsId")
    @ApiModelProperty(value="因提货而生成的新仓单")
    private Long splitStoreGoodsId;

    /**
     * 仓单编号
     */
    @TableField(value = "goodsNo")
    @ApiModelProperty(value="仓单编号")
    private String goodsNo;

    /**
     * 存货凭证编号
     */
    @TableField(value = "inventoryNo")
    @ApiModelProperty(value="存货凭证编号")
    private String inventoryNo;

    /**
     * 收货地址Id
     */
    @TableField(value = "addressId")
    @ApiModelProperty(value="收货地址Id")
    private Long addressId;

    @TableField(value = "userId")
    @ApiModelProperty(value="用户Id")
    private Long userId;

    /**
     * 运费
     */
    @TableField(value = "logisticsCosts")
    @ApiModelProperty(value="运费")
    private BigDecimal logisticsCosts;

    @TableField(value = "otherCosts")
    @ApiModelProperty(value="其他费用")
    private BigDecimal otherCosts;

    /**
     * 证件类型(1身份证)
     */
    @TableField(value = "papersType")
    @ApiModelProperty(value="证件类型(1身份证)")
    private Integer papersType;

    /**
     * 证件号码
     */
    @TableField(value = "papersNumber")
    @ApiModelProperty(value="证件号码")
    private String papersNumber;

    /**
     * 审核人
     */
    @TableField(value = "auditUser")
    @ApiModelProperty(value="审核人")
    private String auditUser;

    /**
     * 审核人
     */
    @TableField(value = "outStoreAuditUser")
    @ApiModelProperty(value="审核人")
    private String outStoreAuditUser;

    /**
     * 状态 1通过 2不通过 3待审核
     */
    @TableField(value = "auditStatus")
    @ApiModelProperty(value="状态 1通过 2不通过 3待审核 4已出库")
    private Integer auditStatus;

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

    /**
     * 配送方式
     */
    @TableField(value = "expressType")
    @ApiModelProperty(value="配送方式：1平台配送2自提")
    private Integer expressType;

    public static final String COL_TXNID = "txnId";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_STOREGOODSID = "storeGoodsId";

    public static final String COL_GOODSNO = "goodsNo";

    public static final String COL_INVENTORYNO = "inventoryNo";

    public static final String COL_ADDRESSID = "addressId";

    public static final String COL_LOGISTICSCOSTS = "logisticsCosts";

    public static final String COL_PAPERSTYPE = "papersType";

    public static final String COL_PAPERSNUMBER = "papersNumber";

    public static final String COL_AUDITUSER = "auditUser";

    public static final String COL_AUDITSTATUS = "auditStatus";

    public static final String COL_AUDITOPINION = "auditOpinion";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}