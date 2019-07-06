package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.shop.view.CategoryDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import io.alpha.app.core.base.BaseObject;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@TableName("category")
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Category extends BaseObject {

    @Id
    @JsonSerialize(using = SerializableSerializer.class)
    private Long id;

    private String txnId;
    /**
     * 品类标题
     */
    private String title;
    /**
     * 品类上级ID
     */
    private Long parentId;
    /**
     * 图片
     */
    private String imgs;
    /**
     * 状态 -1 -逻辑删除 1 -成功 2 -失败
     */
    @TableField(value = "`status`")
    private Integer status;
    /**
     * 排序
     */
    @TableField(value = "`index`")
    private Integer index;
    /**
     * 是否显示导航栏 1 -显示 2 -不显示
     */
    private Integer active;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 创建人
     */
    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;

    public static final CategoryDTO of(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(category, categoryDTO);
        return categoryDTO;
    }
}
