package com.yanolja.yanolja.domain.booking.exception;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class BookingException extends HttpStatusCodeException {
    private final ErrorCode errorCode;

    public BookingException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
