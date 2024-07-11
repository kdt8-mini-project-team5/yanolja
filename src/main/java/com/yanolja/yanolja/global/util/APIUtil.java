package com.yanolja.yanolja.global.util;

import com.yanolja.yanolja.global.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j(topic = "responseLog")
public class APIUtil {

    public static <T> ResponseEntity<T> OK(T data) {
        log.info("OK DATA : ", data);
        return ResponseEntity.ok().body(data);
    }

    public static ResponseEntity OK(String msg) {
        log.info("OK MSG : ", msg);
        return ResponseEntity.ok().body(msg);
    }

    public static ResponseEntity OK() {
        log.info("OK");
        return ResponseEntity.ok().build();
    }


    public static ResponseEntity ERROR(HttpStatusCodeException ex) {
        log.warn(ex.getStatusText());
        return ResponseEntity
                .status(ex.getStatusCode().value())
                .body(new ErrorResponse(ex.getStatusText()));
    }

}
