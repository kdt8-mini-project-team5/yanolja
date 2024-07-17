package com.yanolja.yanolja.domain.booking.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode implements ErrorCode {
    ROOM_NOT_FOUND("객실 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WRONG_OPTIONS("잘못된 옵션이 선택되어 있습니다.", HttpStatus.BAD_REQUEST),

    CONFLICT_BOOKING("예약이 불가능한 날자가 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
    TIMEOUT("요청시간이 만료되었습니다.", HttpStatus.GATEWAY_TIMEOUT),
    WAIT("잠시 후 다시 시도해 주세요.", HttpStatus.BAD_REQUEST)
    ;

    private final String msg;
    private final HttpStatus statusCode;

}