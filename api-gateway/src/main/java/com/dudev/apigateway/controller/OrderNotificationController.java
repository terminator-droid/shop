package com.dudev.apigateway.controller;

import com.dudev.apigateway.feign.NotificationClient;
import com.dudev.notificationservice.api.OrdersApi;
import com.dudev.notificationservice.api.dto.OrderDto;
import com.dudev.notificationservice.api.dto.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер, предоставляющий API для получения уведомлений о заказах
 * из микросервиса {@code notification-service}.
 *
 * <p>Все методы делегируют вызовы клиенту {@link com.dudev.apigateway.feign.NotificationClient}.
 *
 * @see com.dudev.apigateway.feign.NotificationClient
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class OrderNotificationController implements OrdersApi {

    private final NotificationClient notificationClient;

    @Override
    public ResponseEntity<List<OrderDto>> findAll() {
        return notificationClient.findAll();
    }

    @Override
    public ResponseEntity<List<OrderItemDto>> findAllByOrderId(UUID orderId) {
        return notificationClient.findAllByOrderId(orderId);
    }

    @Override
    public ResponseEntity<List<OrderDto>> findAllByUserId(UUID userId) {
        return notificationClient.findAllByUserId(userId);
    }
}
