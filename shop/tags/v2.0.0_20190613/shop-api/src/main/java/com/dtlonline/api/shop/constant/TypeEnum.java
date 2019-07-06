package com.dtlonline.api.shop.constant;

public enum TypeEnum {

    PRODUCT_SPOT(1,"现货"),

    PRODUCT_LOGISTICS(2,"物流"),

    PRODUCT_STORE(3,"仓储"),

    PRODUCT_AUCTION(4,"竞拍"),

    PRODUCT_GOODS(5,"平台仓单"),

    PRODUCT_GROUP_BUY(10,"团购活动"),

    PRODUCT_GROUP(11,"团购商品"),

    PRODUCT_DEMAND(97,"需求"),

    PRODUCT_SUPPLY(98,"供应"),

    PRODUCT_PURCHASE(99,"采购");


    private Integer value;

    private String meaning;

    public Integer getValue(){return this.value;}

    public String getMeaning(){return this.meaning;}

    TypeEnum(Integer value,String meaning){
        this.value = value;
        this.meaning = meaning;
    }
    public static TypeEnum getProductTypeEnum(Integer value){
        TypeEnum [] pt = TypeEnum.values();
        for (TypeEnum p : pt){
            if (p.value.equals(value)){
                return p;
            }
        }
        return PRODUCT_SPOT;
    }
}
