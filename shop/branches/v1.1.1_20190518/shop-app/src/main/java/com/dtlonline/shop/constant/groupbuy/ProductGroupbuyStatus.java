package com.dtlonline.shop.constant.groupbuy;

import org.apache.commons.lang3.StringUtils;

public enum ProductGroupbuyStatus {

    DEL(-1, "已删除"), NEW(1, "待审核"), PAS(2, "通过"), REJ(3, "不通过"), OFF(4, "已下架");

    private Integer code;

    private String message;

    ProductGroupbuyStatus(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {

        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (ProductGroupbuyStatus status : ProductGroupbuyStatus.values()) {
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
