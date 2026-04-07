package com.dudev.apigateway.interceptor;

import com.dudev.apigateway.util.JwtContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * Feign‑интерсептор, проксирующий JWT из {@link com.dudev.apigateway.util.JwtContextHolder}
 * в заголовок {@code Authorization}.
 *
 * @since 1.0.0
 */

@Component
public class JwtPropagationInterceptor implements RequestInterceptor {

    public static final String HEADER_NAME = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwt = JwtContextHolder.getJwt();
        if (jwt != null) {
            requestTemplate.header(HEADER_NAME, BEARER_PREFIX + jwt);
        }
    }
}