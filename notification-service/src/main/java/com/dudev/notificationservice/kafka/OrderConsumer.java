package com.dudev.notificationservice.kafka;

import com.dudev.kafka.dto.OrderCreateEvent;
import com.dudev.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka‑консюмер, получающий события {@link com.dudev.kafka.dto.OrderCreateEvent}
 * и сохраняющий их в базе данных.
 *
 * <p>Использует ручное подтверждение (manual ack) для гарантии,
 * что сообщение будет считано только после успешного сохранения.
 *
 * @see com.dudev.notificationservice.service.OrderService
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order-created-events",
            groupId = "notification-service-1",
            containerFactory = "manualAckFactory")
    public void processOrder(OrderCreateEvent orderCreateEvent, Acknowledgment ack) {
        log.info("Received message with correlationId =" + orderCreateEvent.getCorrelationId());
        orderService.save(orderCreateEvent.getOrderDto());
        ack.acknowledge();
    }
}