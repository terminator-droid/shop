package com.dudev.notificationservice.mapper;

import com.dudev.notificationservice.api.dto.OrderItemDto;
import com.dudev.notificationservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct‑маппер, преобразующий сущность {@link com.dudev.notificationservice.model.OrderItem}
 * в DTO {@link com.dudev.notificationservice.api.dto.OrderItemDto} и обратно.
 *
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    @Mapping(target = "name", source = "productName")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtoList(List<OrderItem> orderItems);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    OrderItem toEntity(com.dudev.kafka.dto.OrderItemDto orderItemDto);
}
