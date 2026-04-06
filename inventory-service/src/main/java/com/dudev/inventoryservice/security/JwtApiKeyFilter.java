package com.dudev.inventoryservice.security;

import com.dudev.inventoryservice.context.UserContextHolder;
import com.dudev.inventoryservice.service.JwtService;
import com.dudev.inventoryservice.util.ApiKeyProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP‑фильтр, поддерживающий два типа аутентификации:
 *
 * <ul>
 *   <li>JWT‑токен в заголовке {@code Authorization: Bearer ...}</li>
 *   <li>API‑ключ в заголовке {@code X-API-Key}</li>
 * </ul>
 *
 * <p>При успешном JWT‑аутентификации заполняет {@link com.dudev.inventoryservice.context.UserContextHolder}
 * данными пользователя.
 *
 * @see com.dudev.inventoryservice.service.JwtService
 * @see com.dudev.inventoryservice.util.ApiKeyProperties
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
public class JwtApiKeyFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApiKeyProperties apiKeyProperties;

    public static final String HEADER_NAME = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String API_KEY_HEADER_NAME = "X-API-Key";

    private final AntPathMatcher apiMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_NAME);

        if (!apiMatcher.matchStart("/api/**", request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isEmpty(authHeader) || !Strings.CI.startsWith(authHeader, BEARER_PREFIX)) {
            String apiKey = request.getHeader(API_KEY_HEADER_NAME);
            if (StringUtils.isEmpty(apiKey) || !apiKeyProperties.getApiKeys().contains(apiKey)) {
                throw new AuthenticationCredentialsNotFoundException("Invalid or missing API key");
            }
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(BEARER_PREFIX.length());

        String username = jwtService.extractUserName(jwt);
        UUID userId = jwtService.extractUserId(jwt);
        UserContextHolder.setUser(userId, username);

        filterChain.doFilter(request, response);
    }
}
