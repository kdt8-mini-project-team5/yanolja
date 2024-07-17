package com.yanolja.yanolja.domain.cart.exception;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class CartException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public CartException(ErrorCode errorCode) {
        super(errorCode.getStatusCode());
        this.errorCode = errorCode;
    }

}
