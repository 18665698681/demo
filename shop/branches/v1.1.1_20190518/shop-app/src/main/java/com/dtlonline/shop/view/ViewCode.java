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

    PRODUCT_IMG("07006","图片上传失败"),

    SHOP_FAILURE("070099", "暂不支持此次状态查询"),

    // --------------团购相关异常-------------------

    GROUP_BUY_NOT_EXISTS("070400", "当前团购活动不存在"),
    GROUP_BUY_IS_SELL_OFF("070410", "当前团购活动已经被下架"),
    GROUP_BUY_AUDIT_IN("070420", "当前活动正在审核中"),
    GROUP_BUY_AUDIT_PASS("070430", "当前活动已审核通过"),
    GROUP_BUY_SET_TOP("070440", "只能置顶审核通过的团购活动"),

    GROUP_BUY_ADD_YEAR_ERROR("070500", "团购年份不能小于当前年份"),
    GROUP_BUY_ADD_MONTH_ERROR("070510", "团购月份不能小于当前月份"),
    GROUP_BUY_ADD_MARKET_PRICE_IS_NULL("070520", "请输入市场价格"),
    GROUP_BUY_ADD_INTENTION_BUY_END_TIME_IS_ERROR("070530", "报名截止日期必须为目标团购月份的上月15号"),
    GROUP_BUY_ADD_AGREEMENT_BUY_END_TIME_IS_ERROR("070531", "报名截止日期的月份必须为团购月份的上一个月"),
    GROUP_BUY_ADD_END_TIME_IS_ERROR("070540", "报名截止日期必须在活动结束时间之前"),

    GROUP_BUY_EDIT_ID_IS_NULL("070600", "团购活动ID不能为空"),
    GROUP_BUY_EDIT_CATEGORY("070610", "当前团购活动已经审核通过，不允许修改货物品类"),
    GROUP_BUY_EDIT_BALANCE_TYPE("070620", "当前团购活动已经审核通过，不允许修改结算方式"),
    GROUP_BUY_EDIT_YEAR("070630", "当前团购活动已经审核通过，不允许修改团购年份"),
    GROUP_BUY_EDIT_MONTH("070640", "当前团购活动已经审核通过，不允许修改团购月份"),
    GROUP_BUY_EDIT_MIN_DISCOUNT_WEIGHT("070650", "当前团购活动已经审核通过，不允许修改单品牌达标量"),
    GROUP_BUY_EDIT__MINTRADE("070660", "当前团购活动已经审核通过，不允许修改最小起团量");

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
