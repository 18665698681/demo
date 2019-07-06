package com.dtlonline.api.shop.command;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShopPageQueryDTO <T> extends Page<T> {
    @JsonIgnore
    private String name;
    @JsonIgnore
    private Integer shopType;
}
