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
@ApiModel(value="com.dtlonline.store.model.store.StoreGoods")
@Data
@TableName(value = "store_goods")
public class StoreGoods {
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
     * pid
     */
    @TableField(value = "pid")
    @ApiModelProperty(value="pid")
    private Long pid;

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
     * 商品Id  from  product
     */
    @TableField(value = "productId")
    @ApiModelProperty(value="商品Id  from  product")
    private Long productId;

    /**
     * 商品Id  from  product
     */
    @TableField(value = "productInfo")
    @ApiModelProperty(value="商品规格信息{'categoryId':10,'standardIds':[11,12,13]}")
    private String productInfo;

    /**
     * 商品Id  from  product
     */
    @TableField(value = "productTitle")
    @ApiModelProperty(value="商品规格信息中文")
    private String productTitle;

    /**
     * 数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="数量")
    private Integer quantity;

    /**
     * 冻结数  借出方审核通过
     */
    @TableField(value = "validQuantity")
    @ApiModelProperty(value="冻结数  借出方审核通过")
    private Integer validQuantity;

    /**
     * 仓单持有人Id
     */
    @TableField(value = "userId")
    @ApiModelProperty(value="仓单持有人Id")
    private Long userId;

    /**
     * 仓库
     */
    @TableField(value = "storeId")
    @ApiModelProperty(value="仓库")
    private Long storeId;

    /**
     * 仓储费
     */
    @TableField(value = "storageFee")
    @ApiModelProperty(value="仓储费")
    private BigDecimal storageFee;

    /**
     * 预计入库日期
     */
    @TableField(value = "inStoreDate")
    @ApiModelProperty(value="预计入库日期")
    private LocalDate inStoreDate;

    /**
     * 车辆车牌
     */
    @TableField(value = "carNumber")
    @ApiModelProperty(value="车辆车牌")
    private String carNumber;

    /**
     * 损耗标准
     */
    @TableField(value = "lossSale")
    @ApiModelProperty(value="损耗标准")
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
     * 状态 1有效 2无效
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态 1有效 2无效")
    private Integer status;

    /**
     * 业务类型(1入库单2借出3借入4买入5卖出6交换7归还8提单)
     */
    @TableField(value = "bizType")
    @ApiModelProperty(value="业务类型(1入库单2借出3借入4买入5卖出6交换7归还8提单)")
    private Integer bizType;

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

    public static final String COL_PID = "pid";

    public static final String COL_GOODSNO = "goodsNo";

    public static final String COL_INVENTORYNO = "inventoryNo";

    public static final String COL_PRODUCTID = "productId";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_VALIDQUANTITY = "validQuantity";

    public static final String COL_USERID = "userId";

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

    public static final String COL_STATUS = "status";

    public static final String COL_BIZTYPE = "bizType";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}