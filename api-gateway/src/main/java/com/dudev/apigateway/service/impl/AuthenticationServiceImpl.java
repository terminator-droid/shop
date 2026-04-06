package com.dudev.apigateway.service.impl;


import com.dudev.apigateway.api.dto.AuthTokensResponse;
import com.dudev.apigateway.api.dto.LoginRequest;
import com.dudev.apigateway.api.dto.RefreshTokenRequest;
import com.dudev.apigateway.api.dto.RegisterRequest;
import com.dudev.apigateway.api.dto.UserCreateRequest;
import com.dudev.apigateway.api.dto.UserResponse;
import com.dudev.apigateway.exception.RefreshTokenExpiredException;
import com.dudev.apigateway.model.RefreshToken;
import com.dudev.apigateway.service.AuthenticationService;
import com.dudev.apigateway.service.JwtService;
import com.dudev.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link com.dudev.apigateway.service.AuthenticationService}.
 *
 * <p>Обрабатывает три сценария аутентификации:
 * <ul>
 *   <li><b>register</b> – создаёт нового пользователя, генерирует access‑ и refresh‑токены.</li>
 *   <li><b>login</b> – проверяет credentials через {@link org.springframework.security.authentication.AuthenticationManager},
 *       возвращает пару токенов и сведения о пользователе.</li>
 *   <li><b>refreshAccess</b> – проверяет срок действия refresh‑токена, выдаёт новый access‑токен
 *       и (по необходимости) новый refresh‑токен.</li>
 * </ul>
 *
 * Все токены генерируются сервисом {@link com.dudev.apigateway.service.JwtService},
 * а операции с пользователем – {@link com.dudev.apigateway.service.UserService}.
 *
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthTokensResponse register(RegisterRequest registerRequest) {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                UserCreateRequest.RoleEnum.USER
        );

        userService.createUser(userCreateRequest);

        String accessToken = jwtService.generateAccessToken(userService.loadUserByUsername(registerRequest.getUsername()));
        String refreshToken = jwtService.generateRefreshToken(userService.loadUserByUsername(registerRequest.getUsername()));
        return new AuthTokensResponse(accessToken, refreshToken);
    }

    @Override
    public AuthTokensResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        UserResponse user = userService.findByUsername(loginRequest.getUsername());
        return new AuthTokensResponse(
                jwtService.generateAccessToken(userDetails),
                jwtService.createOrUpdateRefreshToken(user.getId()).getToken()
        );
    }

    @Override
    public AuthTokensResponse refreshAccess(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken currentRefreshToken = jwtService.findByRefreshToken(refreshTokenRequest.getRefreshToken());
        if (currentRefreshToken.isExpired()) {
            throw new RefreshTokenExpiredException();
        }
        UserDetails userDetails = userService.loadUserByUsername(currentRefreshToken.getUser().getUsername());
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken newRefreshToken = jwtService.createOrUpdateRefreshToken(currentRefreshToken.getUser().getId());
        return new AuthTokensResponse(accessToken, newRefreshToken.getToken());
    }
}

