package com.dudev.apigateway.controller;

import com.dudev.apigateway.feign.OrderClient;
import com.dudev.orderservice.api.OrdersApi;
import com.dudev.orderservice.api.dto.CreateOrderRequest;
import com.dudev.orderservice.api.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер запросов, связанных с созданием заказов.
 *
 * <p>Перенаправляет вызов {@code createOrder} к клиенту
 * {@link com.dudev.apigateway.feign.OrderClient}, который в свою очередь
 * отправляет запрос в микросервис {@code order-service}.
 *
 * @see com.dudev.apigateway.feign.OrderClient
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final OrderClient orderClient;

    @Override
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest createOrderRequest) {
        return orderClient.createOrder(createOrderRequest);
    }
}
