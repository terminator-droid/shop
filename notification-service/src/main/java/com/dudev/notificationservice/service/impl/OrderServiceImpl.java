package com.dudev.notificationservice.service.impl;

import com.dudev.notificationservice.api.dto.OrderDto;
import com.dudev.notificationservice.api.dto.OrderItemDto;
import com.dudev.notificationservice.mapper.OrderItemMapper;
import com.dudev.notificationservice.mapper.OrderMapper;
import com.dudev.notificationservice.model.Order;
import com.dudev.notificationservice.repository.OrderItemRepository;
import com.dudev.notificationservice.repository.OrderRepository;
import com.dudev.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Реализация бизнес‑логики чтения и сохранения заказов.
 *
 * <p>Методы {@link #findAll()}, {@link #findAllByOrderId(UUID)} и
 * {@link #findAllByUserId(UUID)} реализуют поиск, используя репозитории
 * и мапперы. Метод {@link #save(com.dudev.kafka.dto.OrderDto)} сохраняет
 * полученный из Kafka DTO {@link com.dudev.kafka.dto.OrderDto} в базу.
 *
 * @see com.dudev.notificationservice.service.OrderService
 * @see com.dudev.notificationservice.mapper.OrderMapper
 * @see com.dudev.notificationservice.mapper.OrderItemMapper
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findAllByOrderId(UUID orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDto> findAllByUserId(UUID userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void save(com.dudev.kafka.dto.OrderDto orderDto) {
        Order entity = orderMapper.toEntity(orderDto);
        orderRepository.save(entity);
    }
}