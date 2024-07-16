package com.yanolja.yanolja.domain.booking.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode implements ErrorCode {

    CONFLICT_BOOKING("예약이 불가능한 날자가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String msg;
    private final HttpStatus statusCode;

}