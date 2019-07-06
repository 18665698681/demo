package com.dtlonline.api.shop.command.store;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
* Created by Mybatis Generator 2019/05/15
*/
@ApiModel(value="com.dtlonline.shop.model.store.StoreGoodsRequirePath")
@Data
@TableName(value = "store_goods_require_path")
public class StoreGoodsRequirePathDTOI {

    @ApiModelProperty(value="途经省份")
    private String province;

    @ApiModelProperty(value="途经城市")
    private String city;
}