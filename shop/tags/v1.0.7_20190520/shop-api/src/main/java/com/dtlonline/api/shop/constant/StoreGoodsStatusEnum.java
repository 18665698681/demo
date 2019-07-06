package com.dtlonline.api.shop.constant;


public enum StoreGoodsStatusEnum {

    /**
     * 上架
     */
    RACK(1,"上架"),
    /**
     * 下架
     */
    UN_RACK(2,"下架");

    //类型
    private final Integer value;
    //描述
    private final String meaning;

    public Integer getValue() {
        return value;
    }

    public String getMeaning() {
        return meaning;
    }

    StoreGoodsStatusEnum(final Integer value, final String meaning){
        this.value = value;
        this.meaning = meaning;
    }

    public static StoreGoodsStatusEnum getProductTypeEnum(Integer value){
        StoreGoodsStatusEnum[] pt = StoreGoodsStatusEnum.values();
        for (StoreGoodsStatusEnum p : pt){
            if (p.value.equals(value)){
                return p;
            }
        }
        return UN_RACK;
    }
}
