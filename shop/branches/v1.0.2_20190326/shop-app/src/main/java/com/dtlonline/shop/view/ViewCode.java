//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dtlonline.shop.view;

public enum ViewCode {
    SHOP_RACK_FAILURE("070001", "已上架的商品不允许再上架"),

    SHOP_UN_RACK_FAILURE("070002", "已下架的商品不允许再下架"),

    SHOP_UN_RACK_LIST_FAILURE("070003", "请先登录再查看已下架的商品"),

    PRODUCT_FAILURE("070004", "商品已下架,请选择其他商品查看"),

    PRODUCT_QUOTE("07005", "今日报价已超过最大次数"),

    SHOP_FAILURE("070099", "暂不支持此次状态查询");

    private String code;

    private String message;

    private ViewCode(String code, String message) {

        this.code = code;
        this.message = message;
    }

    public String getCode() {

        return this.code;
    }

    public String getMessage() {

        return this.message;
    }
}
