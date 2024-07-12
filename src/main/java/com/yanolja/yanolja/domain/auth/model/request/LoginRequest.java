package com.yanolja.yanolja.domain.auth.model.request;

public record LoginRequest(
        String email,
        String password
) {
}
