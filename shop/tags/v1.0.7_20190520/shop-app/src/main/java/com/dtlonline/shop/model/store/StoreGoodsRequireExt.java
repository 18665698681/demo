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

/**
* Created by Mybatis Generator 2019/05/15
*/
@ApiModel(value="com.dtlonline.shop.model.store.StoreGoodsRequireExt")
@Data
@TableName(value = "store_goods_require_ext")
public class StoreGoodsRequireExt {
     @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Long id;

    /**
     * txnId，关联store_goods_require
     */
    @TableField(value = "txnId")
    @ApiModelProperty(value="txnId，关联store_goods_require")
    private String txnId;

    /**
     * 担保方式：1现金担保，2货物担保，3其他担保
     */
    @TableField(value = "guaranteeType")
    @ApiModelProperty(value="担保方式：1现金担保，2货物担保，3其他担保")
    private Integer guaranteeType;

    /**
     * 其他担保信息
     */
    @TableField(value = "guaranteeMessage")
    @ApiModelProperty(value="其他担保信息")
    private String guaranteeMessage;

    /**
     * 归还日期
     */
    @TableField(value = "returnDay")
    @ApiModelProperty(value="归还日期")
    private LocalDate returnDay;

    /**
     * 归还方式：1货物归还，2现金归还，3货物+现金归还
     */
    @TableField(value = "returnType")
    @ApiModelProperty(value="归还方式：1货物归还，2现金归还，3货物+现金归还")
    private Integer returnType;

    @TableField(value = "createTime")
    @ApiModelProperty(value="")
    private LocalDateTime createTime;

    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="")
    private LocalDateTime lastModifyTime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_GUARANTEETYPE = "guaranteeType";

    public static final String COL_GUARANTEEMESSAGE = "guaranteeMessage";

    public static final String COL_RETURNDAY = "returnDay";

    public static final String COL_RETURNTYPE = "returnType";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}