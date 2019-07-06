package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsRequireFuture")
@Data

public class StoreGoodsRequireFutureDTOI {
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
     * 交割月
     */

    @ApiModelProperty(value="交割月")
    private String deliveryMonth;

    /**
     * 交割合约编号
     */

    @ApiModelProperty(value="交割合约编号")
    private String deliveryNo;

    /**
     * 交割仓库
     */

    @ApiModelProperty(value="交割仓库")
    private String deliveryStoreName;

    @ApiModelProperty(value = "仓库地址")
    private String deliveryStoreAddress;

    /**
     * 创建时间
     */

    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */

    @ApiModelProperty(value="最后修改时间")
    private LocalDateTime lastModifyTime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_DELIVERYMONTH = "deliveryMonth";

    public static final String COL_DELIVERYNO = "deliveryNo";

    public static final String COL_deliveryStoreName = "deliveryStoreName";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}