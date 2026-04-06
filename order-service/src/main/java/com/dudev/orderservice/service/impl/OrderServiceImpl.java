package com.dudev.orderservice.service.impl;

import com.dudev.kafka.dto.OrderCreateEvent;
import com.dudev.orderservice.api.dto.CreateOrderRequest;
import com.dudev.orderservice.api.dto.OrderItem;
import com.dudev.orderservice.api.dto.OrderResponse;
import com.dudev.orderservice.context.UserContextHolder;
import com.dudev.orderservice.dto.NotEnoughProduct;
import com.dudev.orderservice.dto.WriteOffProductsRequestDto;
import com.dudev.orderservice.exception.NotEnoughProductsException;
import com.dudev.orderservice.mapper.OrderEventMapper;
import com.dudev.orderservice.mapper.WriteOffProductsRequestMapper;
import com.dudev.orderservice.model.Order;
import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.model.enums.TaskType;
import com.dudev.orderservice.repository.OrderRepository;
import com.dudev.orderservice.service.OrderService;
import com.dudev.orderservice.service.RetryableTaskService;
import com.example.grpc.InventoryServiceGrpc;
import com.example.grpc.ProductRequest;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductsRequest;
import com.example.grpc.ProductsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация бизнес‑логики создания заказа.
 *
 * <p>Процесс включает:
 * <ul>
 *   <li>Проверку наличия товаров в сервисе Inventory через gRPC.</li>
 *   <li>Валидацию количества и формирование {@link NotEnoughProductsException}
 *       при недостатке.</li>
 *   <li>Расчёт общей стоимости заказа.</li>
 *   <li>Сохранение {@link Order} в базе данных.</li>
 *   <li>Создание двух {@link RetryableTask}:
 *       <ul>
 *         <li>отправка события о созданном заказе в сервис Notification;</li>
 *         <li>списание товаров в сервисе Inventory.</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p>Все операции выполняются в одной транзакции, гарантируя атомарность
 * создания заказа и планирования задач.
 *
 * @see com.dudev.orderservice.api.dto.CreateOrderRequest
 * @see com.dudev.orderservice.api.dto.OrderResponse
 * @see RetryableTaskService
 * @see OrderEventMapper
 * @see WriteOffProductsRequestMapper
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub;
    private final OrderRepository orderRepository;
    private final OrderEventMapper orderEventMapper;
    private final WriteOffProductsRequestMapper writeOffProductsRequestMapper;
    private final RetryableTaskService retryableTaskService;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        List<NotEnoughProduct> notEnoughProducts = new ArrayList<>();
        Map<UUID, Integer> quantities = createOrderRequest.getItems()
                .stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

        List<ProductResponse> checkedProducts = checkAvailability(createOrderRequest)
                .getProductsList()
                .stream()
                .peek(productResponse -> {
                    Integer requestedQuantity = quantities.get(UUID.fromString(productResponse.getId()));
                    int availableQuantity = productResponse.getQuantity();
                    if (requestedQuantity > availableQuantity) {
                        notEnoughProducts.add(new NotEnoughProduct(
                                productResponse.getName(),
                                requestedQuantity,
                                availableQuantity));
                    }
                })
                .toList();

        if (!notEnoughProducts.isEmpty()) {
            throw new NotEnoughProductsException(notEnoughProducts);
        }

        Double totalPrice = checkedProducts.stream()
                .map(p -> quantities.get(UUID.fromString(p.getId())) * p.getPrice() * (1.0 - (p.getSale() / 100.0)))
                .reduce(0.0, Double::sum);

        List<com.dudev.orderservice.model.OrderItem> orderItems = checkedProducts.stream()
                .map(it -> com.dudev.orderservice.model.OrderItem.builder()
                        .productId(UUID.fromString(it.getId()))
                        .productName(it.getName())
                        .price(it.getPrice())
                        .quantity(quantities.get(UUID.fromString(it.getId())))
                        .subtotal(quantities.get(UUID.fromString(it.getId())) * (it.getPrice() * (1.0 - (it.getSale() / 100))))
                        .build())
                .toList();

        Order order = Order.builder()
                .items(new ArrayList<>())
                .userId(UserContextHolder.getUserId())
                .totalPrice(totalPrice)
                .build();

        orderItems.forEach(order::addItem);

        orderRepository.save(order);


        UUID correlationId = UUID.randomUUID();
        createRetryableTasks(order, correlationId);

        return new OrderResponse(correlationId);
    }

    private void createRetryableTasks(Order order, UUID correlationId) {
        OrderCreateEvent orderCreateEvent = new OrderCreateEvent(orderEventMapper.toEventDto(order), correlationId);
        WriteOffProductsRequestDto writeOffDto = writeOffProductsRequestMapper.toWriteOffDto(order);
        try {
            RetryableTask notificationTask = RetryableTask.builder()
                    .type(TaskType.SEND_ORDER_CREATED_EVENT_TO_NOTIFICATION)
                    .status(TaskStatus.IN_PROGRESS)
                    .payload(objectMapper.writeValueAsString(orderCreateEvent))
                    .retryTime(Instant.now().plus(5, ChronoUnit.SECONDS))
                    .build();
            RetryableTask inventoryTask = RetryableTask.builder()
                    .type(TaskType.SEND_WRITE_OFF_EVENT_TO_INVENTORY)
                    .status(TaskStatus.IN_PROGRESS)
                    .payload(objectMapper.writeValueAsString(writeOffDto))
                    .retryTime(Instant.now().plus(5, ChronoUnit.SECONDS))
                    .build();
            retryableTaskService.createTasks(notificationTask, inventoryTask);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ProductsResponse checkAvailability(CreateOrderRequest createOrderRequest) {
        List<ProductRequest> productsInOrder = createOrderRequest.getItems()
                .stream()
                .map(item -> ProductRequest.newBuilder()
                        .setId(item.getProductId().toString())
                        .build())
                .toList();

        ProductsRequest productsOrderRequest = ProductsRequest.newBuilder()
                .addAllProducts(productsInOrder).build();
        return inventoryServiceBlockingStub.checkAvailability(productsOrderRequest);
    }
}