package com.dtlonline.api.shop.constant;

//供需类型
public enum StoreGoodsRequireTradeTypeEnum {

    
    LENDING(1, "【可借】"),
    BORROW(2, "【要借】"),
    EXCHANGE(3, "【可换】"),
    SELL_OUT(4, "【可卖】"),
    BUY_IN(5, "【要买】");

    private Integer value;
    private String message;

    StoreGoodsRequireTradeTypeEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static String getMessage(Integer value){
        if (value==null) {
            return "";
        }
        for (StoreGoodsRequireTradeTypeEnum storeGoodsRequireTradeTypeStatus : StoreGoodsRequireTradeTypeEnum.values()) {
            if (storeGoodsRequireTradeTypeStatus.value.equals(value)) {
                return storeGoodsRequireTradeTypeStatus.getMessage();
            }
        }
        return "";
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
