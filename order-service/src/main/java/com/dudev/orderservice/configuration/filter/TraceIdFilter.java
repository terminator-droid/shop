package com.dudev.orderservice.configuration.filter;

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

@Component
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "X-Trace-Id";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(TRACE_ID))
                .ifPresentOrElse(it -> MDC.put(TRACE_ID, it),
                        () -> MDC.put(TRACE_ID, UUID.randomUUID()));
        response.addHeader(TRACE_ID,(String) MDC.get(TRACE_ID));
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}