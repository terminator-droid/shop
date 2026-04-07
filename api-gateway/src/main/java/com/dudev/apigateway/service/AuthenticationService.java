package com.dudev.apigateway.service;


import com.dudev.apigateway.api.dto.AuthTokensResponse;
import com.dudev.apigateway.api.dto.LoginRequest;
import com.dudev.apigateway.api.dto.RefreshTokenRequest;
import com.dudev.apigateway.api.dto.RegisterRequest;

/**
 * Сервис аутентификации: регистрация, вход и обновление access‑токена.
 *
 * @since 1.0.0
 */

public interface AuthenticationService {

    AuthTokensResponse register(RegisterRequest registerRequest);

    AuthTokensResponse refreshAccess(RefreshTokenRequest refreshTokenRequest);

    AuthTokensResponse login(LoginRequest loginRequest);
}
