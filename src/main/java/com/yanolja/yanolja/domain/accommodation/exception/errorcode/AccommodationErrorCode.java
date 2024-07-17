package com.yanolja.yanolja.domain.accommodation.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccommodationErrorCode implements ErrorCode {

    NOT_FOUND("숙박 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_DATE("체크인 또는 체크아웃 날짜가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    SEARCH_ERROR("숙박 정보 검색 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String msg;
    private final HttpStatus statusCode;

}