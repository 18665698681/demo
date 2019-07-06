package com.dtlonline.api.shop.view.store;


import com.dtlonline.api.shop.command.ProductDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoods")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StoreGoodsDTO {
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
     * 商品Id  from  product
     */

    @ApiModelProperty(value="商品规格id")
    private String productInfo;

    @ApiModelProperty(value="商品规格标题")
    private String productTitle;


    @ApiModelProperty(value="商品标题-类型")
    private String productTitleCategory;
    @ApiModelProperty(value="商品标题-规格")
    private String productTitleStandards;

    @ApiModelProperty(value="商品规格-类别")
    private String categoryName;
    @ApiModelProperty(value="商品标题-规格")
    private List<ProductStandardRecordDTO> productStandardRecordDTOs;

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
    private LocalDate inStoreDate;

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
    private LocalDate insuranceBeginDate;

    /**
     * 商品保险结束日期
     */

    @ApiModelProperty(value="商品保险结束日期")
    private LocalDate insuranceEndDate;

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

    /**
     * 创建时间
     */

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="商品信息")
    private ProductInfoDTO productDTO;

    @ApiModelProperty(value="仓库名称")
    private String storeName;

    @ApiModelProperty(value="仓库地址")
    private String storeAddress;
    /**
     * 最后更新时间
     */

    @ApiModelProperty(value="最后更新时间")
    private LocalDateTime lastModifyTime;

    @ApiModelProperty(value="统计：剩余可用数量")
    private Integer sumValidQuantity;
    @ApiModelProperty(value="统计：已借出数量")
    private Integer sumLendingQuantity;
    @ApiModelProperty(value="统计：冻结数量数量")
    private Integer sumFrozenQuantity;
    @ApiModelProperty(value="统计：全部资产数量")
    private Integer sumAllQuantity;


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