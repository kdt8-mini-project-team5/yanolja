package com.yanolja.yanolja.global.model.type;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getMsg();
    HttpStatus getStatusCode();

}
