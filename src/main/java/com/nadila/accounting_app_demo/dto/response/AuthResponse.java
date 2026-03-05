package com.nadila.accounting_app_demo.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private final String token;
    private final String tokenType;
    private final String username;
    private final String role;
}