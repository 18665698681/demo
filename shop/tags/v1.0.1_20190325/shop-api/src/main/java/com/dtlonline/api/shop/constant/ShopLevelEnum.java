package com.dtlonline.api.shop.constant;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ShopLevelEnum {
    /**
     * 一级
     */
    ONE(1, "一级店铺"),
    /**
     * 二级
     */
    TWO(2, "二级店铺"),
    /**
     * 三级
     */
    THREE(3, "三级店铺");

    final Integer value;
    @JsonValue
    final String meaning;
    ShopLevelEnum(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public Integer getValue() {
        return value;
    }

    public static ShopTypeEnum convert(String value) throws Exception {
        Integer value2i = Integer.valueOf(value);
        for(ShopTypeEnum shopTypeEnum : ShopTypeEnum.values()){
            if(shopTypeEnum.getValue().equals(value2i)) {
                return shopTypeEnum;
            }
        }
        throw new Exception("没有该传入值的匹配类型");
    }
}
