package com.dudev.orderservice.service.impl;

import com.dudev.orderservice.service.JwtService;
import com.dudev.orderservice.util.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

/**
 * Реализация {@link com.dudev.orderservice.service.JwtService},
 * использующая библиотеку <a href="https://github.com/jwtk/jjwt">jjwt</a>
 * для извлечения имени пользователя и его UUID из JWT‑токена.
 *
 * <p>Ключ подписи берётся из {@link com.dudev.orderservice.util.TokenProperties},
 * что позволяет менять его в конфигурационном файле без перекомпиляции кода.
 *
 * @see com.dudev.orderservice.service.JwtService
 * @see com.dudev.orderservice.util.TokenProperties
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final TokenProperties tokenProperties;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, claims -> claims.get("id", String.class)));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(tokenProperties.getJwt().getSalt()).build().parseClaimsJws(token)
                .getBody();
    }
}