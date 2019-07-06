package com.dtlonline.api.shop.constant.groupbuy;

import org.apache.commons.lang3.StringUtils;

public enum ProductGroupbuyPublishEnum {

    YES(1, "已公示"), NO(2, "未公示");

    private Integer code;

    private String message;

    ProductGroupbuyPublishEnum(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {

        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (ProductGroupbuyPublishEnum type : ProductGroupbuyPublishEnum.values()) {
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
