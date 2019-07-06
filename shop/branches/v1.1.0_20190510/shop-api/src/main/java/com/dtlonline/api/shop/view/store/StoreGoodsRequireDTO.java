package com.dtlonline.api.shop.view.store;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsRequireDTO")
@Data

public class StoreGoodsRequireDTO {
    /**
     * id
     */

    @ApiModelProperty(value="id")
    private Long id;

    /**
     * txnId
     */

    @ApiModelProperty(value="txnId")
    private String txnId;

    /**
     * 发布人Id
     */

    @ApiModelProperty(value="发布人Id")
    private Long userId;

    @ApiModelProperty(value="供应编号")
    private String requireNo;

    /**
     * 仓单Id
     */

    @ApiModelProperty(value="仓单Id")
    private Long storeGoodsId;

    /**
     * 发布商品信息 {"categoryId":123,"standradIds":[12,13,14]  }
     */

    @ApiModelProperty(value="发布商品信息 {'categoryId':123,'standradIds':[12,13,14]  }")
    private String productInfo;

    /**
     * 发布商品信息 {"categoryId":123,"standradIds":[12,13,14]  }
     */

    @ApiModelProperty(value="发布商品信息 {'categoryId':123,'standradIds':[12,13,14]  }")
    private String productTitle;

    @ApiModelProperty(value="商品规格-类别")
    private String categoryName;
    @ApiModelProperty(value="商品标题-规格")
    private List<ProductStandardRecordDTO> productStandardRecordDTOs;

    @ApiModelProperty(value="要换入的规格")
    private List<StoreGoodsProductTitleDTO> storeGoodsProductTitles;


    @ApiModelProperty(value="商品图片")
    private String productImgs;

    @ApiModelProperty(value="商品图片url组")
    private List<String> imgUrls;

    /**
     * 标题
     */

    @ApiModelProperty(value="标题")
    private String title;


    /**
     * 需求类型（共享类型） 1借出 2借入 3交换  4卖出 5买入
     */

    @ApiModelProperty(value="需求类型（共享类型） 1借出 2借入 3交换  4卖出 5买入")
    private Integer tradeType;

    @ApiModelProperty(value = "仓单类型(0无效 1平台仓单 2社会仓单3期货仓单4在途仓单5需求)")
    private Integer type;

    /**
     * 借出、借入、换入、换出的数量
     */

    @ApiModelProperty(value="借出、借入、换入、换出的数量")
    private Integer quantity;

    @ApiModelProperty(value="剩余数量")
    private Integer validQuantity;

    @ApiModelProperty(value="共享价格")
    private BigDecimal unitPrice;

    /**
     * 交易单下架日期
     */

    @ApiModelProperty(value="交易单下架日期")
    private LocalDate offSaleDate;

    /**
     * 每天回报率，每借出去一天就回报多少金额
     */

    @ApiModelProperty(value="每天回报率，每借出去一天就回报多少金额")
    private BigDecimal returnRate;

    @ApiModelProperty(value="交货地区")
    private String tradeZoneAddress;

    /**
     * 商品保险金额
     */

    @ApiModelProperty(value="商品保险金额")
    private BigDecimal insuranceMoney;

    /**
     * 商品保险开始日期
     */

    @ApiModelProperty(value="商品保险开始日期")
    private LocalDateTime insuranceBeginDate;

    /**
     * 商品保险结束日期
     */

    @ApiModelProperty(value="商品保险结束日期")
    private LocalDateTime insuranceEndDate;

    /**
     * 需求商品信息 [{"categoryId":123,"standradIds":[12,13,14]  }  ]
     */

    @ApiModelProperty(value="需求商品信息 [{'categoryId':123,'standradIds':[12,13,14]  }  ]")
    private String requireProductInfo;

    /**
     * 需求商品地区（借入方大概提货区域），只做展示
     */

    @ApiModelProperty(value="需求商品地区（借入方大概提货区域），只做展示")
    private String requireTradeZone;

    /**
     * 平台审核人
     */

    @ApiModelProperty(value="平台审核人")
    private String auditUser;;

    /**
     * 审核意见
     */

    @ApiModelProperty(value="审核意见")
    private String auditOpinion;

    /**
     * 状态 1通过 2不通过 3待审核
     */

    @ApiModelProperty(value="状态 1通过 2不通过 3待审核")
    private Integer auditStatus;

    /**
     * 创建时间
     */

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */

    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value="仓单信息")
    private StoreGoodsDTO storeGoodsDTO;

    @ApiModelProperty(value="在途供应经过路径")
    private String pathJoin;

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

    public static final String COL_AUDITOPINION = "auditOpinion";

    public static final String COL_AUDITSTATUS = "auditStatus";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}