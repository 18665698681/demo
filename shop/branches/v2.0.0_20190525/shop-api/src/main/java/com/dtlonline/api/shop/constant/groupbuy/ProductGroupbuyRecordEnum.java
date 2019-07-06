package com.dtlonline.api.shop.constant.groupbuy;

import org.apache.commons.lang3.StringUtils;

public enum ProductGroupbuyRecordEnum {

    DEL(-1, "已删除"), PAS(1, "审核通过"), REJ(2, "审核不通过"), NEW(3, "待审核"), MAT(4, "待撮合");

    private Integer code;

    private String message;

    ProductGroupbuyRecordEnum(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(Integer code) {

        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (ProductGroupbuyRecordEnum status : ProductGroupbuyRecordEnum.values()) {
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
