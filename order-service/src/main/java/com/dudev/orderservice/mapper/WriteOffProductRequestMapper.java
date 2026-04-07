package com.dudev.orderservice.mapper;

import com.dudev.orderservice.dto.WriteOffProductRequestDto;
import com.dudev.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct‑маппер, переводящий {@link OrderItem}
 * в запрос {@link WriteOffProductRequestDto} для Inventory.
 *
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WriteOffProductRequestMapper {

    WriteOffProductRequestDto toWriteOffDto(OrderItem orderItem);

    List<WriteOffProductRequestDto> toWriteOffDtos(List<OrderItem> orderItems);
}
