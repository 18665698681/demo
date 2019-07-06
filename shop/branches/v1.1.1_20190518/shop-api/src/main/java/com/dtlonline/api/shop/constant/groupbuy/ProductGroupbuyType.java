package com.dtlonline.api.shop.constant.groupbuy;

import org.apache.commons.lang3.StringUtils;

public enum ProductGroupbuyType {

    AGREEMENT(1, "协议"), INTENT(2, "意向");

    private Integer code;

    private String message;

    ProductGroupbuyType(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {

        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (ProductGroupbuyType type : ProductGroupbuyType.values()) {
            if (type.getCode().intValue() == code.intValue()) {
                return type.getMessage();
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
