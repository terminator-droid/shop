package com.dudev.orderservice.service;

import com.dudev.orderservice.api.dto.CreateOrderRequest;
import com.dudev.orderservice.api.dto.OrderResponse;

/**
 * Сервисный слой, отвечающий за бизнес‑логику работы с заказами.
 *
 * <p>Метод {@code createOrder} принимает DTO запроса и возвращает DTO ответа.
 *
 * @see com.dudev.orderservice.api.dto.CreateOrderRequest
 * @see com.dudev.orderservice.api.dto.OrderResponse
 * @since 1.0.0
 */

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest createOrderRequest);
}
