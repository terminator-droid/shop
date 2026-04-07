package com.dudev.apigateway.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Фильтр, генерирующий и распространяющий Trace‑ID для каждого HTTP‑запроса.
 *
 * <p>Если клиент передал заголовок {@code X-Trace-Id}, его значение помещается в
 * MDC (Mapped Diagnostic Context) и в ответный заголовок. Если заголовка нет,
 * генерируется случайный UUID и используется аналогично. Это позволяет
 * трассировать запросы сквозь микросервисы, а также включать Trace‑ID в логи
 * благодаря конфигурации логгера.</p>
 *
 * <p>После завершения обработки запроса MDC очищается, чтобы данные не «просочились»
 * в последующие запросы в том же потоке.</p>
 *
 * @see org.jboss.logging.MDC
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(TRACE_ID))
                .ifPresentOrElse(it -> MDC.put(TRACE_ID, it),
                        () -> MDC.put(TRACE_ID, UUID.randomUUID().toString()));
        response.addHeader(TRACE_ID,(String) MDC.get(TRACE_ID));
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}