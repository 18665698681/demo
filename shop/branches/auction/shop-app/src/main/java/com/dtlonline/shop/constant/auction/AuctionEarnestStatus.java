package com.dtlonline.shop.constant.auction;

public enum AuctionEarnestStatus {

    WAIT(1, "待缴纳"), SUCCESS(2, "已冻结"), OVER(3, "已释放");

    private Integer code;

    private String message;

    private AuctionEarnestStatus(int code, String message) {

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
