package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.command.StandardParamDTO;
import com.dtlonline.api.shop.command.StandardRecordDTO;
import io.alpha.app.core.base.BasePageObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoods")
@Data

public class StoreGoodsDTOI extends BasePageObject {
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
     * pid
     */

    @ApiModelProperty(value="pid")
    private Long pid;

    /**
     * 仓单编号
     */

    @ApiModelProperty(value="仓单编号")
    private String goodsNo;

    /**
     * 存货凭证编号
     */

    @ApiModelProperty(value="存货凭证编号")
    private String inventoryNo;

    /**
     * 商品Id  from  product
     */

    @ApiModelProperty(value="商品Id  from  product")
    private Long productId;

    /**
     * 数量
     */

    @ApiModelProperty(value="数量")
    private Integer quantity;

    /**
     * 冻结数  借出方审核通过
     */

    @ApiModelProperty(value="冻结数  借出方审核通过")
    private Integer validQuantity;

    /**
     * 仓单持有人Id
     */

    @ApiModelProperty(value="仓单持有人Id")
    private Long userId;

    /**
     * 仓库
     */

    @ApiModelProperty(value="仓库")
    private Long storeId;

    /**
     * 仓储费
     */

    @ApiModelProperty(value="仓储费")
    private BigDecimal storageFee;

    /**
     * 预计入库日期
     */

    @ApiModelProperty(value="预计入库日期")
    private LocalDateTime inStoreDate;

    /**
     * 车辆车牌
     */

    @ApiModelProperty(value="车辆车牌")
    private String carNumber;

    /**
     * 损耗标准
     */

    @ApiModelProperty(value="损耗标准")
    private BigDecimal lossSale;

    /**
     * 保兑人签章
     */

    @ApiModelProperty(value="保兑人签章")
    private String confirmer;

    /**
     * 保管人签章
     */

    @ApiModelProperty(value="保管人签章")
    private String preserver;

    /**
     * 关联合同
     */

    @ApiModelProperty(value="关联合同")
    private String contract;

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
     * 状态 1有效 2无效
     */

    @ApiModelProperty(value="状态 1有效 2无效")
    private Integer status;

    /**
     * 业务类型(1入库单2借出3借入4买入5卖出6交换7归还8提单)
     */

    @ApiModelProperty(value="业务类型(1入库单2借出3借入4买入5卖出6交换7归还8提单)")
    private Integer bizType;

    @ApiModelProperty(value="商品规格")
    private String productTitle;
    /**
     * 创建时间
     */

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

//    @ApiModelProperty(value="商品标题筛选")
//    private String categoryName;
//
//    @ApiModelProperty(value="商品规格筛选")
//    private List<StandardRecordDTO> standardRecords;

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