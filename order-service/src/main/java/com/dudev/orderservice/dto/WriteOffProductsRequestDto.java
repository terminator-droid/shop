package com.dudev.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO, группирующий несколько запросов на списание товаров.
 *
 * <p>Внутри хранится список {@link WriteOffProductRequestDto},
 * который будет передан в микросервис Inventory.
 *
 * @see WriteOffProductRequestDto
 * @author ussdanil
 * @since 1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffProductsRequestDto {

    private List<WriteOffProductRequestDto> writeOffProductRequestDtos;
}

