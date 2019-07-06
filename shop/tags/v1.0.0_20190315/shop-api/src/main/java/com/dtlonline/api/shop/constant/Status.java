package com.dtlonline.api.shop.constant;

public enum Status {
    /**
     * 通过
     */
    PASS(1, "通过"),
    /**
     * 不通过
     */
    NOT_PASS(2, "不通过"),
    /**
     * 待审核
     */
    PENDING(3,"待审核"),
    /**
     * 未认证
     */
    NOT_AUTH(4,"未认证");

    final Integer value;
    final String meaning;

    Status(Integer value, String meaning) {
        this.value = value;
        this.meaning = meaning;
    }

    public Integer getValue() {
        return value;
    }

    public String getMeaning() {
        return meaning;
    }

    public static Status getStatusType(Integer value){
        Status [] status = Status.values();
        for (Status s: status){
            if (s.getValue().equals(value)){
                return s;
            }
        }
        return PENDING;
    }
}
