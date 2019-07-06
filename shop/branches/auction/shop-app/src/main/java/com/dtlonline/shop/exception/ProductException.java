package com.dtlonline.shop.exception;


import io.alpha.core.exception.FastRuntimeException;

public class ProductException extends FastRuntimeException {
    public static final long serialVersionUID =1L;

    public ProductException(String code,String msg){
        super(code,msg);
    }
}
