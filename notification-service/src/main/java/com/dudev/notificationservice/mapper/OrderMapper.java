package com.dudev.notificationservice.mapper;

import com.dudev.notificationservice.api.dto.OrderDto;
import com.dudev.notificationservice.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct‑маппер, отвечающий за преобразование заказов
 * между доменной моделью {@link com.dudev.notificationservice.model.Order},
 * DTO {@link com.dudev.notificationservice.api.dto.OrderDto} и
 * внешним DTO {@link com.dudev.kafka.dto.OrderDto}.
 *
 * <p>После маппинга {@link #linkItems(Order)} связывает дочерние
 * {@link com.dudev.notificationservice.model.OrderItem} с их родителем.
 *
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "orderItemDtos", source = "items")
    OrderDto toDto(Order order);

    List<OrderDto> toDtoList(List<Order> orders);

    @Mapping(target = "items", source = "orderItemDtos")
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "items", source = "orderItemDtos")
    Order toEntity(com.dudev.kafka.dto.OrderDto orderDto);

    List<Order> toEntityList(List<OrderDto> orderDtos);

    @AfterMapping
    default void linkItems(@MappingTarget Order order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }
    }
}
