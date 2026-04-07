package com.dudev.orderservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Включает планировщик Spring {@code @Scheduled}.
 *
 * <p>Без отдельного класса‑конфигурации планировщик отключён по умолчанию.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Configuration
@EnableScheduling
public class SchedulerConfiguration {
}
