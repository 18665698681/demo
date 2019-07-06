package com.dtlonline.api.shop.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductQuoteDTO {

    /**
     * 业务唯一标识
     */
    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;
    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    /**
     * 报价价格
     */
    @NotNull(message = "报价不能为空")
    private BigDecimal price;
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    private Integer amount;

}
