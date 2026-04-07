package com.dudev.orderservice.mapper;

import com.dudev.orderservice.dto.WriteOffProductRequestDto;
import com.dudev.orderservice.dto.WriteOffProductsRequestDto;
import com.dudev.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * MapStruct‑маппер, собирающий запрос на списание всех товаров заказа.
 *
 * <p>Конвертирует {@link Order#items} в список {@link WriteOffProductRequestDto}
 * с помощью {@link WriteOffProductRequestMapper}.
 *
 * @see WriteOffProductRequestMapper
 * @since 1.0.0
 */

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = WriteOffProductRequestMapper.class)
public interface WriteOffProductsRequestMapper {

    @Mapping(target = "writeOffProductRequestDtos", source = "items")
    WriteOffProductsRequestDto toWriteOffDto(Order order);
}
