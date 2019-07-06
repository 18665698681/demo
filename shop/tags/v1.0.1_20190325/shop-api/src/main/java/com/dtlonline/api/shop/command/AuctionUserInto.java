package com.dtlonline.api.shop.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class AuctionUserInto {
    /**
     * 业务唯一标识
     */
    @NotBlank(message = "业务流水号不能为空")
    private String txnId;

    @NotNull(message = "产品Id")
    private Long productId;

    @NotNull(message = "收货地址Id")
    private Long addressId;
}