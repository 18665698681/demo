package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("product_recommend")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ProductRecommend {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 业务唯一标识
     */
    private String txnId;
    /**
     * 推荐商品唯一标识
     */
    private String productTxnId;
    /**
     * 推荐权重
     */
    private Integer weights;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;
}
