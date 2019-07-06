//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dtlonline.shop.constant.store;

public enum StoreGoodsViewCode {

    NOT_BELONG_USER("080001", "这个仓库或仓单不属于您，清重新选择"),
    CONCURRENT_OPERATE("080002", "其他用户也同时才做了这个仓单，请重试"),
    ERROR_FLOW("080003", "错误的流程"),
    LACK_STANDARD("080004", "至少选择一个规格或者一个仓单"),
    LACK_IMAGES("080005", "必须上传图片"),
    DISABLE_STORE_GOODS("080006", "此仓单未审核，不可操作"),
    STANDARD_EXCESS_COUNT_LIMIT("080007", "规格过多，清减少到3个以下"),
    INSUFFICIENT_QUANTITY("080008", "仓库数量不足");

    private String code;

    private String message;

    private StoreGoodsViewCode(String code, String message) {

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
