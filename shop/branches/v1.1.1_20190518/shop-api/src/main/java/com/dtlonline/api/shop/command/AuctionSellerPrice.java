package com.dtlonline.api.shop.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author gaoqiang
 * @Description
 * @Date 17:43 2019/3/7
 **/

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSellerPrice {

    @NotNull(message = "产品Id")
    private Long productId;

    @NotNull(message = "出价金额")
    @Min(value = 1, message = "错误的出价金额")
    private BigDecimal price;

}