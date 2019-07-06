package com.dtlonline.shop.constant.auction;

public enum AuctionUserStatus {

    GOINGON(1, "竞拍中"), SUCCESS(2, "竞拍成功"), OVER(3, "竞拍结束"), ALL(3, "全部");

    private Integer code;

    private String message;

    private AuctionUserStatus(int code, String message) {

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
