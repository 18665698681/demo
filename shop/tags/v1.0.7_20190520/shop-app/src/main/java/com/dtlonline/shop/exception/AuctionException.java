package com.dtlonline.shop.exception;

import io.alpha.app.core.exception.FastRuntimeException;

public class AuctionException extends FastRuntimeException {

    private static final long serialVersionUID = 1L;

    public AuctionException(final String code, final String msg) {
        super(code, msg);
    }

}
