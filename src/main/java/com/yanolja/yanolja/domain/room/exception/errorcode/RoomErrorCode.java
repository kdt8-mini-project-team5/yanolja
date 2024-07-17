package com.yanolja.yanolja.domain.room.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode {
    ROOM_NOT_FOUND("객실 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String msg;
    private final HttpStatus statusCode;

}
