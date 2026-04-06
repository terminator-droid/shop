package com.dudev.apigateway.controller;

import com.dudev.apigateway.api.AuthApi;
import com.dudev.apigateway.api.dto.AuthTokensResponse;
import com.dudev.apigateway.api.dto.LoginRequest;
import com.dudev.apigateway.api.dto.RefreshTokenRequest;
import com.dudev.apigateway.api.dto.RegisterRequest;
import com.dudev.apigateway.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST‑контроллер, реализующий {@link com.dudev.apigateway.api.AuthApi}.
 *
 * <p>Обрабатывает запросы аутентификации: вход в систему, обновление токена
 * доступа и регистрацию новых пользователей.
 *
 * @see com.dudev.apigateway.service.AuthenticationService
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthTokensResponse> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @Override
    public ResponseEntity<AuthTokensResponse> refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshAccess(refreshTokenRequest));
    }

    @Override
    public ResponseEntity<AuthTokensResponse> register(RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }
}
