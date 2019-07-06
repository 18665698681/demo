package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("attention")
public class Attention {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String txnId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 关注ID
     */
    private Long targetId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建日期
     */
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    private LocalDateTime lastModifyTime;

}
