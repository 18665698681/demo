package com.dtlonline.shop.constant.auction;

public enum AuctionProcessStatus {

    BEFORE(1, "未开始"), GOINGON(2, "进行中"), OVER(3, "已结束");

    private Integer code;

    private String message;

    private AuctionProcessStatus(int code, String message) {

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
