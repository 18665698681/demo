package com.dtlonline.shop.constant.auction;

public enum AuctionAuditStatus {

    PASS(1, "通过"), NOT_PASS(2, "未通过"), WAIT_AUDIT(3, "待审核");

    private Integer code;

    private String message;

    private AuctionAuditStatus(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public Integer getCode() {

        return this.code;
    }

    public String getMessage() {

        return this.message;
    }
}
