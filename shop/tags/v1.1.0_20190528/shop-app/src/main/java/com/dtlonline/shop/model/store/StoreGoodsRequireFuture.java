package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.store.model.store.StoreGoodsRequireFuture")
@Data
@TableName(value = "store_goods_require_future")
public class StoreGoodsRequireFuture {
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
     * 交割月，已废弃，使用deliverDay
     */
    @TableField(value = "deliveryMonth")
    @ApiModelProperty(value="交割月，已废弃，使用deliverDay")
    private String deliveryMonth;

    /**
     * 交割合约编号
     */
    @TableField(value = "deliveryNo")
    @ApiModelProperty(value="交割合约编号")
    private String deliveryNo;

    /**
     * 交割仓库
     */
    @TableField(value = "deliveryStoreName")
    @ApiModelProperty(value="交割仓库")
    private String deliveryStoreName;

    @TableField(value = "deliveryStoreAddress")
    @ApiModelProperty(value = "仓库地址")
    private String deliveryStoreAddress;

    @TableField(value = "deliveryTime")
    @ApiModelProperty(value="交割日期，替代deliveryMonth")
    private LocalDateTime deliveryTime;


    @TableField(value = "createTime")
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="最后修改时间")
    private LocalDateTime lastModifyTime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_DELIVERYMONTH = "deliveryMonth";

    public static final String COL_DELIVERYNO = "deliveryNo";

    public static final String COL_DELIVERYSTORENAME = "deliveryStoreName";

    public static final String COL_DELIVERYSTOREADDRESS = "deliveryStoreAddress";

    public static final String COL_DELIVERYDAY = "deliveryDay";


    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}