package com.yanolja.yanolja.global.handler.exception;

import com.yanolja.yanolja.global.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class HandlerException {

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity authException(HttpStatusCodeException e) {

        log.error(e.getStatusText());

        return ResponseEntity.status(e.getStatusCode())
            .body(new ErrorResponse(e.getStatusText()));
    }
}
