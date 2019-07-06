package com.dtlonline.api.shop.command;

import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryPackDTO extends BaseObject {

    private String txnId;
    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    private String title;
    /**
     * 父类ID
     */
    private Long parentId;

    @NotNull(message = "类型不能为空")
    private Integer type;
    /**
     * 图片
     */
    @NotEmpty(message = "图片不能为空")
    private List imgList;
    /**
     * 状态
     */
    @NotNull(message = "发布状态不能为空")
    private Integer status;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer index;
    /**
     * 是否显示导航栏 1 -显示 2 -不显示
     */
    @NotNull(message = "设置导航栏显示不能为空")
    private Integer active;

}
