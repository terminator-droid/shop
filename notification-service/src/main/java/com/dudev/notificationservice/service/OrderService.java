package com.dudev.notificationservice.service;

import com.dudev.notificationservice.api.dto.OrderDto;
import com.dudev.notificationservice.api.dto.OrderItemDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> findAll();

    List<OrderItemDto> findAllByOrderId(UUID orderId);

    List<OrderDto> findAllByUserId(UUID userId);

    void save(com.dudev.kafka.dto.OrderDto orderDto);
}
