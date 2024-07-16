package com.yanolja.yanolja.domain.room.exception;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import org.springframework.web.client.HttpStatusCodeException;

public class RoomException extends HttpStatusCodeException {

    private final ErrorCode errorCode;

    public RoomException(ErrorCode errorCode) {
        super(errorCode.getStatusCode(), errorCode.getMsg());
        this.errorCode = errorCode;
    }

}
