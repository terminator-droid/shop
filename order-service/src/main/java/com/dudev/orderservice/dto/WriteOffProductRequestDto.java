package com.dudev.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO запроса на списание конкретного продукта из склада.
 *
 * <p>Содержит идентификатор продукта и количество, которое необходимо
 * списать. Используется в запросе {@link WriteOffProductsRequestDto}.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffProductRequestDto {

    private String productId;
    private int quantity;
}
