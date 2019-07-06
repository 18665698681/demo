package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Standard;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StandardDTO extends BaseObject {
    @JsonProperty("standardId")
    private Long id;
    /**
     * 规格名称
     */
    private String title;
    /**
     * 输入方式  1 -文本框 2 -多选项 3 -单选项
     */
    private Integer type;
    /**
     * 数据
     */
    private String data;
    /**
     * 1 -必选 2 -非必选
     */
    private Integer required;

    public static final StandardDTO of(Standard standard){
        if (standard == null){
            return null;
        }
        StandardDTO dto = new StandardDTO();
        BeanUtils.copyProperties(standard,dto);
        return dto;
    }
}
