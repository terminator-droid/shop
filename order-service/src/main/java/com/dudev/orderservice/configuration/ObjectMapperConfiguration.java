package com.dudev.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация {@link ObjectMapper} для JSON‑сериализации/десериализации.
 *
 * <p>Регистрирует модуль Java 8 Time и отключает запись дат в виде timestamps,
 * чтобы JSON содержал читаемые ISO‑8601 строки.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
