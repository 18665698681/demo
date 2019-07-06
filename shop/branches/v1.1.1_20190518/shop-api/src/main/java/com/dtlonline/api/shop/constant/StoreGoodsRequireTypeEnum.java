package com.dtlonline.api.shop.constant;

//仓单类型
public enum StoreGoodsRequireTypeEnum {

    INVALID(0, "无效", ""),
    PLATFORM(1, "平台仓单", "DTLQHGY"),
    SOCIAL(2, "社会仓单", "DTLSHGY"),
    FUTURE(3, "期货仓单", "DTLQHGY"),
    IN_WAY(4, "在途仓单", "DTLZTGY"),
    REQUIRE(5, "需求", "DTLHWXQ");

    private Integer value;
    private String message;
    private String transactionNoPrefix;

    StoreGoodsRequireTypeEnum(Integer value, String message,String transactionNoPrefix) {
        this.value = value;
        this.message = message;
        this.transactionNoPrefix = transactionNoPrefix;
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

    public String getTransactionNoPrefix() {
        return transactionNoPrefix;
    }

    public void setTransactionNoPrefix(String transactionNoPrefix) {
        this.transactionNoPrefix = transactionNoPrefix;
    }
}
