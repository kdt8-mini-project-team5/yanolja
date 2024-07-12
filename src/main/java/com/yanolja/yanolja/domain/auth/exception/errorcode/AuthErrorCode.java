package com.yanolja.yanolja.domain.auth.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    LOGIN_FAILED("로그인에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("유저를 찾을수 없습니다.", HttpStatus.NOT_FOUND)
    ;

    private final String msg;
    private final HttpStatus statusCode;
}
