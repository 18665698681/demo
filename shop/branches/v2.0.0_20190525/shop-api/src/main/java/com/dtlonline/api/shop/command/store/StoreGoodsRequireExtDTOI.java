package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/05/15
*/
@ApiModel(value="com.dtlonline.shop.model.store.StoreGoodsRequireExt")
@Data
@TableName(value = "store_goods_require_ext")
public class StoreGoodsRequireExtDTOI {

    @ApiModelProperty(value="担保方式：1现金担保，2货物担保，3其他担保")
    private Integer guaranteeType;

    @ApiModelProperty(value="其他担保信息")
    private String guaranteeMessage;

    @ApiModelProperty(value="归还日期")
    private LocalDate returnDay;

    @ApiModelProperty(value="归还方式：1货物归还，2现金归还，3货物+现金归还")
    private Integer returnType;
}