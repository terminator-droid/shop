package com.dudev.apigateway.feign;


import com.dudev.orderservice.api.dto.CreateOrderRequest;
import com.dudev.orderservice.api.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign‑клиент для создания заказов в микросервисе {@code order-service}.
 *
 * @see com.dudev.orderservice.api.dto.CreateOrderRequest
 * @see com.dudev.orderservice.api.dto.OrderResponse
 * @since 1.0.0
 */

@FeignClient(name = "order-client")
public interface OrderClient {

    @PostMapping("/api/order")
    ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest);
}