package com.dtlonline.shop.model.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
* Created by Mybatis Generator 2019/05/15
*/
@ApiModel(value="com.dtlonline.shop.model.store.StoreGoodsRequirePath")
@Data
@TableName(value = "store_goods_require_path")
public class StoreGoodsRequirePath {
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
     * 途经省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value="途经省份")
    private String province;

    /**
     * 途经城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value="途经城市")
    private String city;

    @TableField(value = "createTime")
    @ApiModelProperty(value="")
    private LocalDateTime createtime;

    @TableField(value = "lastModifyTime")
    @ApiModelProperty(value="")
    private LocalDateTime lastmodifytime;

    public static final String COL_TXNID = "txnId";

    public static final String COL_PROVINCE = "province";

    public static final String COL_CITY = "city";

    public static final String COL_CREATETIME = "createTime";

    public static final String COL_LASTMODIFYTIME = "lastModifyTime";
}