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

    public abstract String getKey(String addtionKey);
}
