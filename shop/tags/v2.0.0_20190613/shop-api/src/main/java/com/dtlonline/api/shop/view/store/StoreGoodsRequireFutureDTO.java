package com.dtlonline.api.shop.view.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsRequireFuture")
@Data

public class StoreGoodsRequireFutureDTO {
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
     * 交割月，已废弃，使用deliverDay
     */

    @ApiModelProperty(value="交割月，已废弃，使用deliverDay")
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

    @NotBlank(message = "交割仓库地址")
    private String deliveryStoreAddress;

    @ApiModelProperty(value="交割日期，替代deliveryMonth")
    private LocalDateTime deliveryTime;


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

}