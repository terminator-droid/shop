package com.dudev.apigateway.service.impl;


import com.dudev.apigateway.exception.RefreshTokenNotFoundException;
import com.dudev.apigateway.exception.UserNotFoundException;
import com.dudev.apigateway.model.RefreshToken;
import com.dudev.apigateway.model.User;
import com.dudev.apigateway.repository.RefreshTokenRepository;
import com.dudev.apigateway.repository.UserRepository;
import com.dudev.apigateway.service.JwtService;
import com.dudev.apigateway.service.UserService;
import com.dudev.apigateway.util.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Реализация {@link com.dudev.apigateway.service.JwtService}.
 *
 * <p>Отвечает за работу с JWT‑ и refresh‑токенами:
 * <ul>
 *   <li>извлечение имени пользователя и проверка валидности токена;</li>
 *   <li>создание access‑токенов с пользовательскими claim‑ами (id, role);</li>
 *   <li>создание, поиск, обновление и удаление refresh‑токенов в базе;</li>
 *   <li>проверка истечения срока действия токенов.</li>
 * </ul>
 *
 * Ключ подписи берётся из {@link com.dudev.apigateway.util.TokenProperties},
 * а доступ к пользователям и токенам осуществляется через репозитории
 * {@link com.dudev.apigateway.repository.UserRepository} и
 * {@link com.dudev.apigateway.repository.RefreshTokenRepository}.
 *
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenProperties tokenProperties;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, tokenProperties.getJwt().getExpiration());
    }

    private String generateToken(UserDetails userDetails, Integer expiration) {
        Map<String, Object> claims = new HashMap<>();
        User fullUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
        claims.put("id", fullUser.getId());
        claims.put("role", fullUser.getRole());
        return generateToken(claims, userDetails, expiration);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Integer expiration) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getJwt().getSalt());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, tokenProperties.getRefresh().getExpiration());
    }

    @Override
    public RefreshToken findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    @Transactional
    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        RefreshToken token = RefreshToken.builder()
                .expiryDate(Instant.now().plus(Duration.of(tokenProperties.getRefresh().getExpiration(), ChronoUnit.MILLIS)))
                .user(user)
                .token(generateRefreshToken(userDetails))
                .build();
        return refreshTokenRepository.save(token);
    }

    @Transactional
    @Override
    public RefreshToken createOrUpdateRefreshToken(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return refreshTokenRepository.findByUserId(user.getId())
                .map(existing -> updateRefreshToken(userId))
                .orElseGet(() -> createRefreshToken(userId));
    }

    @Transactional
    @Override
    public RefreshToken updateRefreshToken(UUID userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(it -> {
                    refreshTokenRepository.delete(it);
                    refreshTokenRepository.flush();
                    return RefreshToken.builder()
                            .user(it.getUser())
                            .token(generateRefreshToken(userService.loadUserByUsername(it.getUser().getUsername())))
                            .expiryDate(Instant.now().plus(Duration.of(tokenProperties.getRefresh().getExpiration(), ChronoUnit.MILLIS)))
                            .build();
                })
                .orElseThrow(RefreshTokenNotFoundException::new);
        return refreshTokenRepository.save(refreshToken);
    }
}
