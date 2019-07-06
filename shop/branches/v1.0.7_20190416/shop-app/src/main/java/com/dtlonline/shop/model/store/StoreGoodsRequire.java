package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.store.model.store.StoreGoodsRequire")
@Data
@TableName(value = "store_goods_require")
public class StoreGoodsRequire {
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
     * 发布人Id
     */
    @TableField(value = "userId")
    @ApiModelProperty(value="发布人Id")
    private Long userId;

    /**
     * 仓单Id
     */
    @TableField(value = "storeGoodsId")
    @ApiModelProperty(value="仓单Id")
    private Long storeGoodsId;

    /**
     * 发布商品信息 {"categoryId":123,"standradIds":[12,13,14]  }
     */
    @TableField(value = "productInfo")
    @ApiModelProperty(value="发布商品信息 {'categoryId':123,'standradIds':[12,13,14]  }")
    private String productInfo;

    /**
     * 发布商品信息 {"categoryId":123,"standradIds":[12,13,14]  }
     */
    @TableField(value = "productTitle")
    @ApiModelProperty(value="发布商品信息中文")
    private String productTitle;

    @TableField(value = "productImgs")
    @ApiModelProperty(value="商品图片")
    private String productImgs;

    /**
     * 标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="标题")
    private String title;

    @TableField(value = "type")
    @ApiModelProperty(value = "仓单类型(0无效 1平台仓单 2社会仓单3期货仓单4在途仓单5需求)")
    private Integer type;

    /**
     * 需求类型（共享类型） 1借出 2借入 3交换  4卖出 5买入
     */
    @TableField(value = "tradeType")
    @ApiModelProperty(value="需求类型（共享类型） 1借出 2借入 3交换  4卖出 5买入")
    private Integer tradeType;

    /**
     * 借出、借入、换入、换出的数量
     */
    @TableField(value = "quantity")
    @ApiModelProperty(value="借出、借入、换入、换出的数量")
    private Integer quantity;

    @TableField(value = "unitPrice")
    @ApiModelProperty(value="共享价格")
    private BigDecimal unitPrice;

    /**
     * 交易单下架日期
     */
    @TableField(value = "offSaleDate")
    @ApiModelProperty(value="交易单下架日期")
    private LocalDate offSaleDate;

    /**
     * 每天回报率，每借出去一天就回报多少金额
     */
    @TableField(value = "returnRate")
    @ApiModelProperty(value="每天回报率，每借出去一天就回报多少金额")
    private BigDecimal returnRate;

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

    @TableField(value = "requireProductInfo")
    @ApiModelProperty(value="需求商品信息 [{'categoryId':123,'standradIds':[12,13,14]  }  ]")
    private String requireProductInfo;

    @TableField(value = "requireProductTitle")
    @ApiModelProperty(value="需求商品信息中文")
    private String requireProductTitle;

    /**
     * 需求商品地区（借入方大概提货区域），只做展示
     */
    @TableField(value = "requireTradeZone")
    @ApiModelProperty(value="需求商品地区（借入方大概提货区域），只做展示")
    private String requireTradeZone;

    /**
     * 平台审核人
     */
    @TableField(value = "auditUser")
    @ApiModelProperty(value="平台审核人")
    private String auditUser;


    /**
     * 审核意见
     */
    @TableField(value = "auditOpinion")
    @ApiModelProperty(value="审核意见")
    private String auditOpinion;

    /**
     * 状态 1通过 2不通过 3待审核
     */
    @TableField(value = "auditStatus")
    @ApiModelProperty(value="状态 1通过 2不通过 3待审核")
    private Integer auditStatus;

    @TableField(value = "storeAttr")
    @ApiModelProperty(value = "仓库属性 1厂库 2中转库 3口岸仓库 4保税仓")
    private Integer storeAttr;

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

    public static final String COL_USERID = "userId";

    public static final String COL_STOREID = "storeId";

    public static final String COL_GOODSINFO = "goodsInfo";

    public static final String COL_TITLE = "title";

    public static final String COL_TYPE = "type";

    public static final String COL_TRADETYPE = "tradeType";

    public static final String COL_QUANTITY = "quantity";

    public static final String COL_OFFSALEDATE = "offSaleDate";

    public static final String COL_RETURNRATE = "returnRate";

    public static final String COL_insuranceMoney = "insuranceMoney";

    public static final String COL_INSURANCEBEGINDATE = "insuranceBeginDate";

    public static final String COL_INSURANCEENDDATE = "insuranceEndDate";

    public static final String COL_requireProductInfo = "requireProductInfo";

    public static final String COL_REQUIRETRADEZONE = "requireTradeZone";

    public static final String COL_AUDITUSER = "auditUser";

    public static final String COL_AUDITSTAFFNAME = "auditStaffName";

    public static final String COL_AUDITOPINION = "auditOpinion";

    public static final String COL_AUDITSTATUS = "auditStatus";

    public static final String COL_STOREATTR = "storeAttr";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}