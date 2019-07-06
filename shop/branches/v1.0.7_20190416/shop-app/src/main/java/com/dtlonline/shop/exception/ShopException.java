package com.dtlonline.shop.exception;

import io.alpha.app.core.exception.FastRuntimeException;

public class ShopException extends FastRuntimeException {

    private static final long serialVersionUID = 1L;

    public ShopException(String code, String msg) {
        super(code, msg);
    }
}