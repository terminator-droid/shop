package com.dudev.apigateway.feign;

import com.dudev.notificationservice.api.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

/**
 * Feign‑клиент для доступа к микросервису {@code notification-service}.
 *
 * <p>Позволяет получать список заказов и их детали.
 *
 * @see com.dudev.notificationservice.api.dto.OrderDto
 * @since 1.0.0
 */

@FeignClient(name = "notification-client")
public interface NotificationClient {

    @GetMapping("/api/orders")
    ResponseEntity<List<OrderDto>> findAll();

    @GetMapping("/api/orders/{orderId}")
    ResponseEntity<List<com.dudev.notificationservice.api.dto.OrderItemDto>> findAllByOrderId(@PathVariable UUID orderId);

    @GetMapping("/api/orders/{userId}")
    ResponseEntity<List<com.dudev.notificationservice.api.dto.OrderDto>> findAllByUserId(@PathVariable UUID userId);
}
