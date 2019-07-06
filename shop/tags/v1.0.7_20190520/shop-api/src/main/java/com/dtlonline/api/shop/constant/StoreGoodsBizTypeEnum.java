package com.dtlonline.api.shop.constant;


public enum StoreGoodsBizTypeEnum {

    //业务类型(1入库单2借出3借入4买入5卖出6交换7归还8提单)
    STORING(1,"入库单"),
    LENDING(2,"借出"),
    BORROW(3,"借入"),
    BUY_IN(4,"买入"),
    SELL_OUT(5,"卖出"),
    EXCHANGE(6,"交换"),
    RETURN(7,"归还"),
    RETRIEVE(8,"提单");

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

    StoreGoodsBizTypeEnum(final Integer value, final String meaning){
        this.value = value;
        this.meaning = meaning;
    }

//    public static StoreGoodsBizTypeEnum getProductTypeEnum(Integer value){
//        StoreGoodsBizTypeEnum[] pt = StoreGoodsBizTypeEnum.values();
//        for (StoreGoodsBizTypeEnum p : pt){
//            if (p.value.equals(value)){
//                return p;
//            }
//        }
//        return UN_RACK;
//    }
}
