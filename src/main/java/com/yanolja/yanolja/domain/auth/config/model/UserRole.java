package com.yanolja.yanolja.domain.auth.config.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN")
    ;

    private final String auth;
}
