package com.dudev.orderservice.kafka;

import com.dudev.kafka.dto.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Сервис‑обёртка над {@link KafkaTemplate}, отправляющий событие
 * {@link OrderCreateEvent} в топик {@code order-created-events}.
 *
 * <p>Метод {@code sendOrderCreated} возвращает {@link CompletableFuture},
 * позволяя асинхронно реагировать на результат отправки.
 *
 * @see KafkaTemplate
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreateEvent> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String orderEventTopic;

    public CompletableFuture<SendResult<String, OrderCreateEvent>> sendOrderCreated(OrderCreateEvent orderCreateEvent) {
        return kafkaTemplate.send(orderEventTopic, orderCreateEvent);
    }
}