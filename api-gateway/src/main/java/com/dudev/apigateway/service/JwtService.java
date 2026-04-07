package com.dudev.apigateway.service;


import com.dudev.apigateway.model.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Сервис управления JWT: генерация, валидация, поиск refresh‑токенов.
 *
 * @since 1.0.0
 */

public interface JwtService {

    String extractUserName(String token);

    String generateAccessToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    String generateRefreshToken(UserDetails userDetails);

    RefreshToken findByRefreshToken(@NotNull String refreshToken);

    @Transactional
    RefreshToken createRefreshToken(UUID userId);

    @Transactional
    RefreshToken createOrUpdateRefreshToken(UUID userId);

    RefreshToken updateRefreshToken(UUID userId);
}
