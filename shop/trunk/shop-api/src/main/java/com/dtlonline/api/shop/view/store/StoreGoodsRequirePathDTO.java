package com.dtlonline.api.shop.view.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class StoreGoodsRequirePathDTO {
    @ApiModelProperty(value="")
    private Long id;

    /**
     * txnId，关联store_goods_require
     */
    @ApiModelProperty(value="txnId，关联store_goods_require")
    private String txnId;

    /**
     * 途经省份
     */
    @ApiModelProperty(value="途经省份")
    private String province;

    /**
     * 途经城市
     */
    @ApiModelProperty(value="途经城市")
    private String city;

    @ApiModelProperty(value="")
    private LocalDateTime createtime;

    @ApiModelProperty(value="")
    private LocalDateTime lastmodifytime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}