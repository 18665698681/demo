package com.dtlonline.api.shop.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.app.core.base.BaseObject;
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
public class CategoryDTOI extends BaseObject {
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
     * 状态 -1-已删除 2-发布 3-未发布
     */
    private Integer status;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 图片
     */
    private String imgs;
    /**
     * 排序
     */
    private Integer index;
    /**
     * 是否显示导航栏 1 -显示 2 -不显示
     */
    private Integer active;

    /**
     * 子品类集合
     */
    private List<CategoryDTOI> ChildCategory;
}
