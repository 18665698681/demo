package com.dtlonline.shop.view;

/**
 * The enum Shop view code.
 *
 * @author Deveik
 * @date 2019 /01/25
 */
public enum ShopViewCode {
    /**
     * 未通过企业认证
     */
    NOT_ENTERPRISE_AUTH_STATUS("787970", "请先完成企业认证"),
    /**
     * 不存在该id
     */
    NOT_EXIST_THIS_ID("717171", "没有该用户");


    private String code;
    private String message;

    ShopViewCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
