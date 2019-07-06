package com.dtlonline.api.shop.constant;


public enum ProductTypeEnum {

    /**
     * 上架
     */
    RACK(1,"上架"),
    /**
     * 下架
     */
    UN_RACK(2,"下架"),
    /**
     * 待上架
     */
    PENDING(3,"待审核");

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

    ProductTypeEnum(final Integer value,final String meaning){
        this.value = value;
        this.meaning = meaning;
    }

    public static ProductTypeEnum getProductTypeEnum(Integer value){
        ProductTypeEnum [] pt = ProductTypeEnum.values();
        for (ProductTypeEnum p : pt){
            if (p.value.equals(value)){
                return p;
            }
        }
        return PENDING;
    }
}
