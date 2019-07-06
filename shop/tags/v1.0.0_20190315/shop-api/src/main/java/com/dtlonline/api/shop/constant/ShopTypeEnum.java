package com.dtlonline.api.shop.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ShopTypeEnum {
    /**
     * 自营店
     */
    SELF_SUPPORT_STORE(1, "自营店"),
    /**
     * 第三方
     */
    THIRD_PARTY_STORE(2, "第三方");


    final Integer value;
    @JsonValue
    final String meaning;
    ShopTypeEnum(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public Integer getValue() {
        return value;
    }

    public String getMeaning() { return meaning; }

    public static ShopTypeEnum convert(String value) throws Exception {
        Integer value2i = Integer.valueOf(value);
        for(ShopTypeEnum shopTypeEnum : ShopTypeEnum.values()){
            if(shopTypeEnum.getValue().equals(value2i)) {
                return shopTypeEnum;
            }
        }
        throw new Exception("转换枚举出现错误");
    }
}
