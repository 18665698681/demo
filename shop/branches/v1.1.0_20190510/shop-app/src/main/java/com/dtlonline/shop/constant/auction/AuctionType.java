package com.dtlonline.shop.constant.auction;

public enum AuctionType {

    UP(1, "向上竞拍"), DOWN(2, "向下竞拍");

    private Integer code;

    private String message;

    private AuctionType(int code, String message) {

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
