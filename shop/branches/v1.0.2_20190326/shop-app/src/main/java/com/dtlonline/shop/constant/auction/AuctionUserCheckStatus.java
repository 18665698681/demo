package com.dtlonline.shop.constant.auction;

import com.google.common.base.Objects;

public enum AuctionUserCheckStatus {

    SUCCESS(1000, "正常"),NO_APPROVE(1001, "未实名认证"), NO_BUY_PRODUCT(1002, "不能参与自己发布的商品");

    private Integer code;

    private String message;

    private AuctionUserCheckStatus(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public Integer getCode() {

        return this.code;
    }

    public String getMessage() {

        return this.message;
    }
    public static AuctionUserCheckStatus valuekOf(Integer flag) {
        for (AuctionUserCheckStatus checkEnum : values()) {
            if (Objects.equal(checkEnum.getCode(),flag)) {
                return checkEnum;
            }
        }
        return null;
    }

}
