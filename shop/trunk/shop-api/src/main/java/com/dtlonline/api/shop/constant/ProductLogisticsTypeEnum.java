package com.dtlonline.api.shop.constant;


public enum ProductLogisticsTypeEnum {

    LOGISTICS_TYPE_LAND(1,"陆运"),

    LOGISTICS_TYPE_SEA(2,"海运"),

    LOGISTICS_TYPE_AIR(3,"空运");

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

    ProductLogisticsTypeEnum(final Integer value, final String meaning){
        this.value = value;
        this.meaning = meaning;
    }

    public static ProductLogisticsTypeEnum getProductLogisticsTypeEnum(Integer value){
        ProductLogisticsTypeEnum[] pt = ProductLogisticsTypeEnum.values();
        for (ProductLogisticsTypeEnum p : pt){
            if (p.value.equals(value)){
                return p;
            }
        }
        return LOGISTICS_TYPE_LAND;
    }
}
