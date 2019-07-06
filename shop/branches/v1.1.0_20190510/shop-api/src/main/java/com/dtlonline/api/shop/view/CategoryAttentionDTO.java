package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("关注对象")
public class CategoryAttentionDTO extends BaseObject {
    /**
     * 品类ID
     */
    @ApiModelProperty("关注对象ID")
    @JsonProperty("targetId")
    private Long id;
    /**
     * 品类名称
     */
    @ApiModelProperty("关注品类名称")
    private String title;
    /**
     * 图片
     */
    @ApiModelProperty("品类封面图")
    private String imgs;
    /**
     * 是否已关注 1-已关注 2-未关注
     */
    @ApiModelProperty("是否已关注 1-已关注 2-未关注")
    private Integer isFollow;
}
