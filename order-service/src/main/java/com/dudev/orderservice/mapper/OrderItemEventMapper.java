package com.dudev.orderservice.mapper;

import com.dudev.kafka.dto.OrderItemDto;
import com.dudev.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct‑маппер, конвертирующий {@link OrderItem}
 * в DTO {@link com.dudev.kafka.dto.OrderItemDto}.
 *
 * <p>Переименовывает поля {@code productName} → {@code name}
 * и {@code productId} → {@code id}.
 *
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemEventMapper {

    @Mapping(target = "name", source = "productName")
    @Mapping(target = "id", source = "productId")
    OrderItemDto toEventDto(OrderItem orderItem);

    List<OrderItemDto> toEventDtoList(List<OrderItem> orderItems);
}
