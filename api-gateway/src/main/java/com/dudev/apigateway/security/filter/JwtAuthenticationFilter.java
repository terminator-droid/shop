package com.dudev.apigateway.security.filter;


import com.dudev.apigateway.service.JwtService;
import com.dudev.apigateway.service.UserService;
import com.dudev.apigateway.util.JwtContextHolder;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр, выполняющий аутентификацию запросов по JWT.
 *
 * <p>Каждый входящий HTTP‑запрос проверяется на наличие заголовка
 * {@code Authorization} с префиксом {@code Bearer }. Если токен присутствует,
 * из него извлекается имя пользователя, проверяется валидность и, при успехе,
 * создаётся {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 * - пользователь считается аутентифицированным. Кроме того, токен сохраняется
 * в {@link com.dudev.apigateway.util.JwtContextHolder} (ThreadLocal) для
 * последующей передачи в Feign‑клиенты.</p>
 *
 * <p>Фильтр наследует {@link org.springframework.web.filter.OncePerRequestFilter},
 * поэтому гарантировано вызывается один раз за запрос.</p>
 *
 * @see com.dudev.apigateway.service.JwtService
 * @see com.dudev.apigateway.service.UserService
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.@NonNull HttpServletResponse response,
                                    jakarta.servlet.@NonNull FilterChain filterChain)
            throws ServletException, IOException {

        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Обрезаем префикс и получаем имя пользователя из токена
        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var username = jwtService.extractUserName(jwt);

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService
                    .loadUserByUsername(username);

            // Если токен валиден, то аутентифицируем пользователя и кладем jwt в threadLocal
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                JwtContextHolder.setJwt(jwt);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
