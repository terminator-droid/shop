package com.dudev.orderservice.mapper;

import com.dudev.kafka.dto.OrderDto;
import com.dudev.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct‑маппер, преобразующий доменную модель {@link Order}
 * в DTO события {@link com.dudev.kafka.dto.OrderDto}.
 *
 * <p>Для вложенных элементов {@code OrderItem} используется
 * {@link OrderItemEventMapper}.
 *
 * @see OrderItemEventMapper
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = OrderItemEventMapper.class)
public interface OrderEventMapper {

    @Mapping(target = "orderItemDtos", source = "items")
    OrderDto toEventDto(Order order);
}
