package com.dudev.orderservice.configuration;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация {@link RestTemplate} с добавлением interceptor‑а,
 * который копирует заголовок {@code X‑Trace‑Id} из MDC в каждый исходящий HTTP‑запрос.
 *
 * <p>Это позволяет сохранять цепочку трассировки при вызовах внешних сервисов.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Configuration
public class RestTemplateConfiguration {

    private static final String TRACE_ID = "X-Trace-Id";

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(TRACE_ID, MDC.get(TRACE_ID));
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}