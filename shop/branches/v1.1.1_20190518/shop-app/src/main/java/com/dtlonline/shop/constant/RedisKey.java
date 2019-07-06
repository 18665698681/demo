package com.dtlonline.shop.constant;

public enum RedisKey {

    /**
     * 限制报价次数
     */
    PRODUCT_QUOTE_KEY {
        @Override
        public String getKey(String userId) {
            return "shop:product:quote:" + userId;
        }
    };
    /**
     * 地址坐标
     */
    public static String PRODUCT_GEO_LOCATION_KEY = "shop:product:point:v1";

    public abstract String getKey(String addtionKey);
}
