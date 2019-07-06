package com.dtlonline.api.shop.constant;


public enum StoreGoodsExtractAuditStatusEnum {

    INVALID(0, "无效"),
    PASS(1, "已通过"),
    NOT_PASS(2, "审核拒绝"),
    PENDING(3,"待审核"),
    OUT_STORE(4,"已出库");

    private final Integer value;
    private final String meaning;

    public Integer getValue() {
        return value;
    }

    public String getMeaning() {
        return meaning;
    }

    StoreGoodsExtractAuditStatusEnum(final Integer value, final String meaning){
        this.value = value;
        this.meaning = meaning;
    }

    public static StoreGoodsExtractAuditStatusEnum getStatusType(Integer value){
        StoreGoodsExtractAuditStatusEnum [] storeGoodsExtractAuditStatusEnum = StoreGoodsExtractAuditStatusEnum.values();
        for (StoreGoodsExtractAuditStatusEnum s: storeGoodsExtractAuditStatusEnum){
            if (s.getValue().equals(value)){
                return s;
            }
        }
        return INVALID;
    }

}
