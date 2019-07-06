package com.dtlonline.api.shop.constant;

public enum ShopStatus {
    /**
     * 通过
     */
    VALID(1, "有效"),
    /**
     * 不通过
     */
    IN_VALID(2, "失效");

    final Integer value;
    final String meaning;

    ShopStatus(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public Integer getValue() {
        return value;
    }

    public String getMeaning() {
        return meaning;
    }
}
