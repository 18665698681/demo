package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("product_quote")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductQuote extends BaseObject {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 业务唯一标识
     */
    private String txnId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 报价价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    private LocalDateTime lastModifyTime;
}
