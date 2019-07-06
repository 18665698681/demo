package com.dtlonline.api.shop.constant;

//仓单类型
public enum StoreGoodsRequireTypeEnum {

    INVALID(0, "无效"),
    PLATFORM(1, "平台仓单"),
    SOCIAL(2, "社会仓单"),
    FUTURE(3, "期货仓单"),
    IN_WAY(4, "在途仓单"),
    REQUIRE(5, "需求");

    private Integer value;
    private String message;

    StoreGoodsRequireTypeEnum(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public static StoreGoodsRequireTypeEnum getRequireTypeEnum(Integer value){
        StoreGoodsRequireTypeEnum[] pt = StoreGoodsRequireTypeEnum.values();
        for (StoreGoodsRequireTypeEnum p : pt){
            if (p.value.equals(value)){
                return p;
            }
        }
        return INVALID;
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
