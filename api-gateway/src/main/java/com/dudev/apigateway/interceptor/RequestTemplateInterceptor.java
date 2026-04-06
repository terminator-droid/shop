package com.dudev.apigateway.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Feign‑интерсептор, добавляющий в каждый запрос заголовок {@code X-Trace-Id}
 * из MDC.
 *
 * @since 1.0.0
 */

@Component
public class RequestTemplateInterceptor implements RequestInterceptor {

    private static final String TRACE_ID = "X-Trace-Id";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.headers().put(TRACE_ID, List.of(MDC.get(TRACE_ID)));
    }
}