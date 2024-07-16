package com.yanolja.yanolja.domain.cart.exception.errorcode;

import com.yanolja.yanolja.global.model.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartErrorCode implements ErrorCode {

    EQUALS_CART_IN_DB("같은 내용의 장바구니가 이미 있습니다.",HttpStatus.BAD_REQUEST),
    FAIL_DELETE("장바구니 삭제에 실패했습니다" ,HttpStatus.BAD_REQUEST),
    ;

    private final String msg;
    private final HttpStatus statusCode;

}
