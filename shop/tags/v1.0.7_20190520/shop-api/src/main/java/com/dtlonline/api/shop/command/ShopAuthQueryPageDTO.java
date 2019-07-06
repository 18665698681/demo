package com.dtlonline.api.shop.command;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShopAuthQueryPageDTO<T> extends Page<T> {
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店铺类型
     */
    private Integer shopType;
    /**
     * 店铺状态
     */
    private Integer status;
}
