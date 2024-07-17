package com.yanolja.yanolja.domain.accommodation.exception;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public AccommodationException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getMsg());
        this.errorCode = errorCode;
    }

}