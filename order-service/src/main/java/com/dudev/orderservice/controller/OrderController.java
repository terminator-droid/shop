package com.dudev.orderservice.controller;

import com.dudev.orderservice.api.OrdersApi;
import com.dudev.orderservice.api.dto.CreateOrderRequest;
import com.dudev.orderservice.api.dto.OrderResponse;
import com.dudev.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST‑контроллер, реализующий API заказов.
 *
 * <p>Принимает запросы от клиентов, делегирует бизнес‑логику
 * {@link OrderService} и возвращает {@link OrderResponse} в теле {@link ResponseEntity}.
 *
 * @see OrderService
 * @author ussdanil
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.createOrder(createOrderRequest));
    }
}