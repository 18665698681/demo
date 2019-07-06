package com.dtlonline.api.shop.command.store;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("仓库列表对象")
public class StoreRepDTO extends BaseObject {

    /**
     * id
     */
    @JsonProperty("storeId")
    @ApiModelProperty("仓库ID")
    private Long id;

    /**
     * 仓库名称
     */
    @ApiModelProperty("仓库名称")
    private String name;

    /**
     * 仓库类型 1自营仓库，2合作仓库，3社会仓库，4其他仓库
     */
    @ApiModelProperty("仓库类型 1自营仓库，2合作仓库，3社会仓库，4其他仓库")
    private Byte type;

    /**
     * 仓库地址
     */
    @ApiModelProperty("仓库地址")
    private String address;

    /**
     * 仓库范围
     */
    @ApiModelProperty("仓库范围")
    private String zone;
}