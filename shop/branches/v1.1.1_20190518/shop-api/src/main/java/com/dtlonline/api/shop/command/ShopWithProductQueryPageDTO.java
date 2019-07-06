package com.dtlonline.api.shop.command;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class ShopWithProductQueryPageDTO<T> extends Page<T> {
    private Integer statusSwitch;
    private Long shopId;
}
