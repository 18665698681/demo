package com.dtlonline.api.shop.command;

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
public class CategoryParamDTO {

    /**
     * 标题
     */
    private String title;
    /**
     * 父类ID
     */
    private Long parentId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer index;
    /**
     * 是否显示导航栏 1 -显示 2 -不显示
     */
    private Integer active;

    private List<StandardParamDTO> standardList;

}
