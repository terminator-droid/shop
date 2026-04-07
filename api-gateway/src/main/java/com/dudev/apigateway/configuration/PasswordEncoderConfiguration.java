package com.dudev.apigateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация Spring Security, предоставляющая {@link org.springframework.security.crypto.password.PasswordEncoder}
 * в виде делегирующего энкодера, поддерживающего несколько алгоритмов.
 *
 * @since 1.0.0
 */

@Configuration
public class PasswordEncoderConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
