package com.dtlonline.shop.exception;


import io.alpha.app.core.exception.FastRuntimeException;

public class StoreGoodsException extends FastRuntimeException {
    public static final long serialVersionUID = 1L;

    public StoreGoodsException(String code, String msg) {
        super(code, msg);
    }
}
