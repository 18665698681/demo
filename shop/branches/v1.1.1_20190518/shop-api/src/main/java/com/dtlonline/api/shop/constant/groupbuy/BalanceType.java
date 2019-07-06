package com.dtlonline.api.shop.constant.groupbuy;

import org.apache.commons.lang3.StringUtils;

public enum BalanceType {

    PAYMENT_TO_DELIVERY(1, "款到发货"), CASH_ON_DELIVERY(2, "货到付款");

    private Integer code;

    private String message;

    BalanceType(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {

        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (BalanceType status : BalanceType.values()) {
            if (status.getCode().intValue() == code.intValue()) {
                return status.getMessage();
            }
        }
        return StringUtils.EMPTY;
    }

    public Integer getCode() {

        return this.code;
    }

    public String getMessage() {

        return this.message;
    }
}
