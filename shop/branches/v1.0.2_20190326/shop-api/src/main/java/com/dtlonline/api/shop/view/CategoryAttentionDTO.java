package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryAttentionDTO extends BaseObject {
    /**
     * 品类ID
     */
    @JsonProperty("categoryId")
    private Long id;
    /**
     * 品类名称
     */
    private String title;
    /**
     * 父类ID
     */
    private Long parentId;
    /**
     * 图片
     */
    private String imgs;
    /**
     * 是否已关注 1-已关注 2-未关注
     */
    private Integer isFollow;
    /**
     * 全部 -1 供应-1 需求-2
     */
    private Integer attentionType;
}
