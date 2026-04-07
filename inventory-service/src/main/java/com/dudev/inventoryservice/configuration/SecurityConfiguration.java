package com.dudev.inventoryservice.configuration;

import com.dudev.inventoryservice.security.JwtApiKeyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация Spring Security для микросервиса Inventory.
 *
 * <p>Отключает CSRF, переводит приложение в stateless‑режим,
 * добавляет фильтр {@link com.dudev.inventoryservice.security.JwtApiKeyFilter}
 * перед {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter},
 * а также открывает публичный доступ к API товаров и swagger‑эндпоинтам.
 *
 * @see com.dudev.inventoryservice.security.JwtApiKeyFilter
 * @since 1.0.0
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtApiKeyFilter jwtApiKeyFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtApiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                            response.getWriter().flush();
                        }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll());
        return http.build();
    }
}