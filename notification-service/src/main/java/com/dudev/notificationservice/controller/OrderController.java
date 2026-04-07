package com.dudev.notificationservice.controller;

import com.dudev.notificationservice.api.OrdersApi;
import com.dudev.notificationservice.api.dto.OrderDto;
import com.dudev.notificationservice.api.dto.OrderItemDto;
import com.dudev.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST‑контроллер, реализующий API для получения заказов.
 *
 * <p>Обеспечивает три метода:
 * <ul>
 *   <li>получение всех заказов;</li>
 *   <li>получение всех позиций заказа по {@code orderId};</li>
 *   <li>получение всех заказов пользователя по {@code userId}.</li>
 * </ul>
 *
 * @see com.dudev.notificationservice.service.OrderService
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<List<OrderDto>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @Override
    public ResponseEntity<List<OrderItemDto>> findAllByOrderId(UUID orderId) {
        return ResponseEntity.ok(orderService.findAllByOrderId(orderId));
    }

    @Override
    public ResponseEntity<List<OrderDto>> findAllByUserId(UUID userId) {
        return ResponseEntity.ok(orderService.findAllByUserId(userId));
    }
}
