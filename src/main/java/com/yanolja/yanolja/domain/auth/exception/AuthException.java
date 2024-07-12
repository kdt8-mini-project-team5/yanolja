package com.yanolja.yanolja.domain.auth.exception;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AuthException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getMsg());
        this.errorCode = errorCode;
    }

}
